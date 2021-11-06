package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults.ResultsBuilder;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public class DDDCombatTracker extends CombatTracker {

	private static Random rand = new Random();
	private static final IClassifier<DamageMap> DAMAGE_CLASSIFIER = new DamageClassifier();
	private static final IClassifier<ArmorMap> ARMOR_CLASSIFIER = new ArmorClassifier();
	private static final IClassifier<MobDefenses> DEFENSES_CLASSIFIER = new DefensesClassifier();
	private static final UUID ARMOR_CALC_UUID = UUID.fromString("72e5859a-02d8-4170-9632-f9786547d697");
	private static final UUID TOUGHNESS_CALC_UUID = UUID.fromString("c19d6077-8772-460e-8250-d7780cbb85ca");

	private DDDDamageType type;
	private CombatContext ctx;
	private ResultsBuilder results;
	private ArmorValues armors;
	private Optional<DamageMap> incomingDamage;

	public DDDCombatTracker(EntityLivingBase entity) {
		super(entity);
		this.clear();
	}

	public void clear() {
		this.type = null;
		this.ctx = null;
		this.armors = null;
		this.incomingDamage = null;
		this.results = new ResultsBuilder();
	}

	public CombatResults getRecentResults() {
		return this.results.build();
	}

	public Optional<DDDDamageType> getTypeLastHitBy() {
		return Optional.ofNullable(this.type);
	}

	public Optional<ArmorValues> getNewArmorValues() {
		return Optional.ofNullable(this.armors);
	}

	public void handleAttackStage(LivingAttackEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		if(this.getIncomingDamage().isPresent() && this.ctx.getShield().isPresent() && this.ctx.getShield().get().hasCapability(ShieldDistribution.cap, null)) {
			DamageMap dmg = this.getIncomingDamage().get();
			ItemStack shield = this.ctx.getShield().get();
			ShieldBlockEvent blockEvt = new ShieldBlockEvent(this.ctx.getImmediateAttacker(), this.ctx.getTrueAttacker(), this.getFighter(), this.ctx.getSource(), dmg, shield);
			if(!MinecraftForge.EVENT_BUS.post(blockEvt) && Sets.intersection(blockEvt.getShieldDistribution().getCategories(), dmg.keySet()).size() > 0) {
				this.getIncomingDamage().map(blockEvt.getShieldDistribution()::block);
				this.results.hasEffectiveShield();
				if(this.ctx.getImmediateAttacker() instanceof EntityLivingBase) {
					EntityLivingBase attacker = (EntityLivingBase) this.ctx.getImmediateAttacker();
					attacker.knockBack(this.getFighter(), 0.5f, attacker.posX - this.getFighter().posX, attacker.posZ - this.getFighter().posZ);
				}
			}
			this.getFighter().resetActiveHand();
		}
	}

	public void handleHurtStage(LivingHurtEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			this.type = m.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(m.size()));
			ArmorMap aMap = ARMOR_CLASSIFIER.classify(this.ctx).orElse(new ArmorMap());
			this.armors = m.keySet().stream().filter((t) -> m.get(t) > 0).reduce(new ArmorValues(), (av, t) -> ArmorValues.merge(av, aMap.get(t)), ArmorValues::merge);
		});
	}

	public void handleDamageStage(LivingDamageEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			DEFENSES_CLASSIFIER.classify(this.ctx).ifPresent((defenses) -> {
				final boolean slyStrike;
				final float bruteForce;
				if(this.ctx.getImmediateAttacker() instanceof EntityLivingBase) {
					EntityLivingBase attacker = (EntityLivingBase) this.ctx.getImmediateAttacker();
					slyStrike = EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, attacker) > 0;
					bruteForce = 0.1f * EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.bruteForce, attacker);
				}
				else {
					slyStrike = false;
					bruteForce = 0.0f;
				}
				if(!slyStrike && defenses.immunities.stream().map(Functions.compose((f) -> f > 0.0f, m::remove)).reduce(Boolean::logicalOr).orElse(false)) {
					this.results.hasImmunity();
				}
				defenses.resistances.forEach((k, v) -> {
					float resist = v > 0 ? Math.max(0.0f, v - bruteForce) : v;
					if(m.computeIfPresent(k, (type, dmg) -> dmg * (1 - resist)) != null) {
						if(resist > 0) {
							this.results.hasResistance();
						}
						else if(resist < 0) {
							this.results.hasWeakness();
						}
					}
				});
				UpdateAdaptiveResistanceEvent updateEvt = new UpdateAdaptiveResistanceEvent(this.ctx.getImmediateAttacker(), this.ctx.getTrueAttacker(), this.getFighter(), this.ctx.getSource(), m, defenses.resistances, defenses.immunities);
				MinecraftForge.EVENT_BUS.post(updateEvt);
				IMobResistances resistances = DDDAPI.accessor.getMobResistances(this.getFighter());
				switch(updateEvt.getResult()) {
					case DEFAULT:
						if(!resistances.hasAdaptiveResistance()) {
							break;
						}
						// FALL_THROUGH
					case ALLOW:
						if(resistances.updateAdaptiveResistance(m)) {
							this.results.wasAdaptive();
						}
					default:
						break;
				}
			});
			this.results.withAmount((float) YMath.sum(m.values()));
		});
	}

	@Override
	public ITextComponent getDeathMessage() {
		return this.getTypeLastHitBy().filter((t) -> ModConfig.dmg.useCustomDeathMessages).map((t) -> DDDRegistries.damageTypes.getDeathMessageForType(t, this.ctx.getSource().getTrueSource(), this.getFighter())).orElse(super.getDeathMessage());
	}

	@Override
	public void reset() {
		this.clear();
		super.reset();
	}

	private Optional<DamageMap> getIncomingDamage() {
		if(this.ctx != null) {
			if(this.incomingDamage == null) {
				return this.incomingDamage = DAMAGE_CLASSIFIER.classify(this.ctx);
			}
			return this.incomingDamage;
		}
		return Optional.empty();
	}
	
	private void updateContextAndDamage(DamageSource newSrc, float amount, @Nullable Entity attacker) {
		if(this.ctx == null) {
			this.ctx = new CombatContext(newSrc, amount, attacker, this.getFighter());
		}
		else if(this.incomingDamage != null) {
			this.getIncomingDamage().ifPresent((map) -> {
				float ratio = (float) (amount / YMath.sum(map.values()));
				if(Math.abs(ratio - 1) >= 0.01) {
					map.keySet().forEach((k) -> map.computeIfPresent(k, (key, val) -> val * ratio));					
				}
			});
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onEntityConstructing(EntityConstructing evt) {
		if(evt.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
			entity._combatTracker = new DDDCombatTracker(entity);
		}
	}

// Lowest because we reset active hands, which means shields "aren't in use" past this point.
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onEntityAttack(LivingAttackEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> tracker.handleAttackStage(evt));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onEntityHurt(LivingHurtEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> {
			tracker.handleHurtStage(evt);
			tracker.getIncomingDamage().map(Functions.compose(YMath::sum, DamageMap::values)).map(Double::floatValue).ifPresent(evt::setAmount);
			tracker.getNewArmorValues().ifPresent((vals) -> {
				Stream.Builder<ItemStack> builder = Stream.builder();
				tracker.getFighter().getArmorInventoryList().forEach(builder::accept);
				ArmorValues currVals = builder.build().filter((stack) -> stack.getItem() instanceof ItemArmor).map(Functions.compose((armor) -> new ArmorValues(armor.damageReduceAmount, armor.toughness), (stack) -> (ItemArmor) stack.getItem())).reduce(ArmorValues::merge).get();
				tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier(ARMOR_CALC_UUID, "DDD Armor Calculations Modifier", vals.getArmor() - currVals.getArmor(), 0));
				tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).applyModifier(new AttributeModifier(TOUGHNESS_CALC_UUID, "DDD Toughness Calculations Modifier", vals.getToughness() - currVals.getToughness(), 0));
			});
		});
	}
	
	//We want to try to act first to remove the armor/toughness attribute modifiers that are no longer needed.
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static final void onEntityDamage(LivingDamageEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> {
			tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_CALC_UUID);
			tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).removeModifier(TOUGHNESS_CALC_UUID);
			tracker.handleDamageStage(evt);
		});
	}
	
	@SubscribeEvent
	public static final void onEntityKnockback(LivingKnockBackEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).map(DDDCombatTracker::getRecentResults).map(Predicates.or(Predicates.and(CombatResults::wasImmunityTriggered, (results) -> results.getAmount() == 0), CombatResults::wasShieldEffective)::test).ifPresent(evt::setCanceled);
	}
}
