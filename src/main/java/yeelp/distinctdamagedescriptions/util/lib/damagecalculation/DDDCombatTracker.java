package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.DDDEffects;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults.ResultsBuilder;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public class DDDCombatTracker extends CombatTracker {

	private static Random rand = new Random();
	private static final IClassifier<DamageMap> DAMAGE_CLASSIFIER = new DamageClassifier();
	private static final IClassifier<ArmorMap> ARMOR_CLASSIFIER = new ArmorClassifier();
	private static final IClassifier<MobDefenses> DEFENSES_CLASSIFIER = new DefensesClassifier();
	private static final EntityEquipmentSlot[] ARMOR_SLOTS = Arrays.stream(EntityEquipmentSlot.values()).filter((ees) -> ees.getSlotType() == EntityEquipmentSlot.Type.ARMOR).toArray(EntityEquipmentSlot[]::new);
	public static final UUID ARMOR_CALC_UUID = UUID.fromString("72e5859a-02d8-4170-9632-f9786547d697");
	public static final UUID TOUGHNESS_CALC_UUID = UUID.fromString("c19d6077-8772-460e-8250-d7780cbb85ca");

	private DDDDamageType type;
	private CombatContext ctx;
	private ResultsBuilder results;
	private ArmorValues armors;
	private ShieldDistribution usedShieldDist;
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
		this.usedShieldDist = null;
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

	public Optional<ShieldDistribution> getCurrentlyUsedShieldDistribution() {
		return Optional.ofNullable(this.usedShieldDist);
	}

	public void handleAttackStage(LivingAttackEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		if(this.getIncomingDamage().isPresent() && this.ctx.getShield().isPresent() && this.ctx.getShield().get().hasCapability(ShieldDistribution.cap, null)) {
			DamageMap dmg = this.getIncomingDamage().get();
			ItemStack shield = this.ctx.getShield().get();
			ShieldBlockEvent blockEvt = DDDHooks.fireShieldBlock(this.ctx.getImmediateAttacker(), this.ctx.getTrueAttacker(), this.getFighter(), this.ctx.getSource(), dmg, shield);
			if(!blockEvt.isCanceled() && Sets.intersection(blockEvt.getShieldDistribution().getCategories(), dmg.keySet()).size() > 0) {
				// Set the currently used shield distribution, but don't actually block damage.
				// We can't do that in LivingAttackEvent, save it and do it first thing in
				// LivingHurtEvent
				this.usedShieldDist = blockEvt.getShieldDistribution();
				if(this.ctx.getImmediateAttacker() instanceof EntityLivingBase) {
					EntityLivingBase attacker = (EntityLivingBase) this.ctx.getImmediateAttacker();
					this.getFighter().blockUsingShield(attacker);
				}
			}
			this.getFighter().resetActiveHand();
		}
	}

	public void handleHurtStage(LivingHurtEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			this.getCurrentlyUsedShieldDistribution().ifPresent((shield) -> {
				shield.block(m);
				this.results.hasEffectiveShield(m);
				this.ctx.getShield().ifPresent((stack) -> stack.damageItem((int) (evt.getAmount() * (this.getRecentResults().getShieldRatio().getAsDouble())), this.getFighter()));
			});
			this.type = m.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(m.size()));
			ARMOR_CLASSIFIER.classify(this.ctx).ifPresent((aMap) -> {
				this.armors = m.keySet().stream().filter((t) -> m.get(t) > 0).reduce(new ArmorValues(), (av, t) -> ArmorValues.merge(av, aMap.get(t)), ArmorValues::merge);
			});
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
				if(!slyStrike && defenses.immunities.stream().map(Functions.compose((f) -> f != null && f > 0.0f, m::remove)).reduce(Boolean::logicalOr).orElse(false)) {
					this.results.hasImmunity();
				}
				defenses.resistances.forEach((k, v) -> {
					float resist = v > 0 ? MathHelper.clamp(v - bruteForce, 0.0f, 1.0f) : v;
					if(m.computeIfPresent(k, (type, dmg) -> dmg * Math.max(1 - resist, 0)) != null) {
						if(resist > 0) {
							this.results.hasResistance();
						}
						else if(resist < 0) {
							this.results.hasWeakness();
						}
					}
				});
				DDDAPI.accessor.getMobResistances(this.getFighter()).ifPresent((resistances) -> {
					UpdateAdaptiveResistanceEvent updateEvt = DDDHooks.fireUpdateAdaptiveResistances(this.ctx.getImmediateAttacker(), this.ctx.getTrueAttacker(), this.getFighter(), this.ctx.getSource(), m, defenses.resistances, defenses.immunities);
					switch(updateEvt.getResult()) {
						case DEFAULT:
							if(!resistances.hasAdaptiveResistance()) {
								break;
							}
							// FALL_THROUGH
						case ALLOW:
							float temp = resistances.getAdaptiveAmount();
							resistances.setAdaptiveAmount(updateEvt.getAdaptiveAmount());
							if(resistances.updateAdaptiveResistance(updateEvt.getDamageToAdaptTo())) {
								this.results.wasAdaptive();
							}
							resistances.setAdaptiveAmount(temp);
							if(this.getFighter() instanceof EntityPlayer) {
								resistances.sync((EntityPlayer) this.getFighter());
							}
						default:
							break;
					}
				});
			});
			this.results.withAmount((float) YMath.sum(m.values()));
		});
	}

	@Override
	public ITextComponent getDeathMessage() {
		return this.getTypeLastHitBy().filter((t) -> ModConfig.core.useCustomDeathMessages).map((t) -> DDDRegistries.damageTypes.getDeathMessageForType(t, this.ctx.getSource().getTrueSource(), this.getFighter())).orElse(super.getDeathMessage());
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
			this.results.withStartingDamage(amount);
		}
		else if(this.incomingDamage != null) {
			this.getIncomingDamage().ifPresent((map) -> {
				float dmg = (float) YMath.sum(map.values());
				if(Math.abs(amount - dmg) >= 0.01 && dmg != 0) {
					float ratio = amount / dmg;
					map.keySet().forEach((k) -> map.computeIfPresent(k, (key, val) -> val * ratio));
				}
			});
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityJoinWorld(EntityJoinWorldEvent evt) {
		if(evt.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
			if(!(entity._combatTracker instanceof DDDCombatTracker)) {
				entity._combatTracker = new DDDCombatTracker(entity);
			}
		}
	}

// Lowest because we reset active hands, which means shields "aren't in use" past this point.
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onEntityAttack(LivingAttackEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> tracker.handleAttackStage(evt));
		DeveloperModeKernel.onAttackCallback(evt);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onEntityHurt(LivingHurtEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> {
			tracker.handleHurtStage(evt);
			tracker.getIncomingDamage().map(Functions.compose(YMath::sum, DamageMap::values)).filter((d) -> !Double.isNaN(d)).map(Double::floatValue).ifPresent(evt::setAmount);
			tracker.getNewArmorValues().ifPresent((vals) -> {
				Arrays.stream(ARMOR_SLOTS).map((slot) -> {
					ItemStack stack = tracker.getFighter().getItemStackFromSlot(slot);
					Item item = stack.getItem();
					ArmorValues aVals = null;
					if(item instanceof ISpecialArmor) {
						ArmorProperties props = ((ISpecialArmor) item).getProperties(tracker.getFighter(), stack, evt.getSource(), evt.getAmount(), slot.getIndex());
						aVals = new ArmorValues((float) props.Armor, (float) props.Toughness);
					}
					else if(item instanceof ItemArmor) {
						ItemArmor armor = (ItemArmor) item;
						aVals = new ArmorValues(armor.damageReduceAmount, armor.toughness);
					}
					return aVals;
				}).filter(Predicates.not(Objects::isNull)).reduce(ArmorValues::merge).ifPresent((aVals) -> {
					tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_CALC_UUID);
					tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).removeModifier(TOUGHNESS_CALC_UUID);
					tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier(ARMOR_CALC_UUID, "DDD Armor Calculations Modifier", vals.getArmor() - aVals.getArmor(), 0));
					tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).applyModifier(new AttributeModifier(TOUGHNESS_CALC_UUID, "DDD Toughness Calculations Modifier", vals.getToughness() - aVals.getToughness(), 0));
				});
			});
		});
		DeveloperModeKernel.onHurtCallback(evt);
	}

	// We want to try to act first to remove the armor/toughness attribute modifiers
	// that are no longer needed.
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static final void onEntityDamage(LivingDamageEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> {
			tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_CALC_UUID);
			tracker.getFighter().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).removeModifier(TOUGHNESS_CALC_UUID);
			tracker.handleDamageStage(evt);
			tracker.getRecentResults().getAmount().ifPresent((d) -> evt.setAmount((float) d));
			DDDEffects.doEffects(evt.getSource().getTrueSource(), tracker.getFighter(), tracker.getRecentResults());
		});
		DeveloperModeKernel.onDamageCallback(evt);
	}

	@SubscribeEvent
	public static final void onEntityKnockback(LivingKnockBackEvent evt) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).map(DDDCombatTracker::getRecentResults).map(Predicates.or(Predicates.and(CombatResults::wasImmunityTriggered, (results) -> results.getAmount().orElse(Double.NaN) == 0), CombatResults::wasShieldEffective)::test).ifPresent(evt::setCanceled);
	}
}
