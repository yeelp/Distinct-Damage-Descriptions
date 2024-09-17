package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.mixin.MixinASMEntityLivingBase;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorClassification;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatContext;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults.ResultsBuilder;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations.DDDEnchantmentInfo;

public final class DDDCombatTracker implements IDDDCombatTracker {

	@CapabilityInject(IDDDCombatTracker.class)
	public static Capability<IDDDCombatTracker> cap;
	
	private final EntityLivingBase fighter;
	private DDDDamageType type = null;
	private ShieldDistribution shieldDist = null;
	private Map<EntityEquipmentSlot, ArmorValues> armorVals = null;
	private DamageMap incomingDamage = null;
	private float damage = 0.0f;
	private ResultsBuilder results = new ResultsBuilder();
	private CombatContext ctx = null;
	private boolean damageClassified = false;
	private ArmorClassification armorClassification;
	
	public DDDCombatTracker(EntityLivingBase fighter) {
		this.fighter = fighter;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == cap ? cap.<T>cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("type", (this.type == null ? DDDBuiltInDamageType.UNKNOWN : this.type).getTypeName());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.type = DDDRegistries.damageTypes.get(nbt.getString("type"));
	}

	@Override
	public EntityLivingBase getFighter() {
		return this.fighter;
	}

	@Override
	public Optional<DDDDamageType> getTypeLastHitBy() {
		return Optional.ofNullable(this.type);
	}

	@Override
	public Optional<ShieldDistribution> getCurrentlyUsedShieldDistribution() {
		return Optional.ofNullable(this.shieldDist);
	}

	@Override
	public Optional<Map<EntityEquipmentSlot, ArmorValues>> getNewDeltaArmorValues() {
		return Optional.ofNullable(this.armorVals);
	}

	@Override
	public Optional<DamageMap> getIncomingDamage() {
		if(this.ctx != null) {
			if(!this.wasDamageClassified()) {
				this.incomingDamage = DDDCombatCalculations.classifyDamage(this.ctx).orElse(null);
				this.damageClassified = true;
			}
			return Optional.ofNullable(this.incomingDamage);
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<ArmorClassification> getArmorClassification() {
		return Optional.ofNullable(this.armorClassification);
	}

	@Override
	public float getDamageReference() {
		return this.damage;
	}

	@Override
	public CombatResults getRecentResults() {
		return this.results.build();
	}

	@Override
	public CombatContext getCombatContext() {
		return this.ctx;
	}

	@Override
	public void setTypeLastHitBy(DDDDamageType type) {
		this.type = type;
	}

	@Override
	public void setUsedShieldDistribution(ShieldDistribution dist) {
		this.shieldDist = dist;
	}

	@Override
	public void setCombatContext(CombatContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void setNewDeltaArmorValues(Map<EntityEquipmentSlot, ArmorValues> vals) {
		this.armorVals = vals;
	}

	@Override
	public void setDamageReference(float amount) {
		this.damage = amount;
	}

	@Override
	public void setIncomingDamage(DamageMap map) {
		this.incomingDamage = map;
	}

	@Override
	public void wipeResults() {
		this.results = new ResultsBuilder();
	}
	

	@Override
	public boolean wasDamageClassified() {
		return this.damageClassified;
	}

	@Override
	public void setDamageClassified(boolean state) {
		this.damageClassified = state;
	}

	@Override
	public void handleAttackStage(LivingAttackEvent evt) {
		this.clear();
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		if(this.getIncomingDamage().isPresent() && DDDCombatCalculations.doShieldCalcs(this.ctx)) {
			DamageMap dmg = this.getIncomingDamage().get();
			ItemStack shield = this.ctx.getShield().get();
			ShieldBlockEvent blockEvt = DDDHooks.fireShieldBlock(this.ctx.getImmediateAttacker(), this.ctx.getTrueAttacker(), this.getFighter(), this.ctx.getSource(), dmg, shield);
			if(!blockEvt.isCanceled() && Sets.intersection(blockEvt.getShieldDistribution().getCategories(), dmg.keySet()).size() > 0) {
				//record shield dist, but the distribution must be applied later. It won't work otherwise.
				this.shieldDist = blockEvt.getShieldDistribution();
				if(this.ctx.getImmediateAttacker() instanceof EntityLivingBase) {
					((MixinASMEntityLivingBase) this.getFighter()).useBlockUsingShield((EntityLivingBase) this.ctx.getImmediateAttacker());
				}
			}
			this.getFighter().resetActiveHand();
		}
	}

	@Override
	public void handleHurtStage(LivingHurtEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			this.getCurrentlyUsedShieldDistribution().ifPresent((shield) -> {
				shield.block(m);
				this.results.hasEffectiveShield(m);
				this.ctx.getShield().ifPresent((stack) -> stack.damageItem((int) Math.ceil(evt.getAmount() * (this.getRecentResults().getShieldRatio().getAsDouble())), this.getFighter()));
			});
			if(!m.isEmpty()) {
				this.type = DDDCombatCalculations.getWeightedRandomRepresentativeType(m);
			}
			if(ModConfig.resist.enableArmorCalcs) {
				DDDCombatCalculations.classifyArmor(this.ctx).ifPresent((classified) -> {
					this.armorClassification = classified;
					Set<DDDDamageType> damagingTypes = m.keySet().stream().filter((t) -> m.get(t) > 0).collect(Collectors.toSet());
					DDDRegistries.damageTypes.getAll().stream().filter(Predicates.not(damagingTypes::contains)).forEach((type) -> {
						classified.forEachArmorMap((slot, map) -> map.remove(type));
					});
					this.armorVals = Maps.newHashMap();
					classified.forEachArmorMap((slot, map) -> {
						this.armorVals.put(slot, ModConfig.resist.armorCalcRule.merge(map.values().stream().map((av) -> av.sub(ModConfig.resist.negativeRule.handlePotentialNegativeArmorValues(classified.getOriginalArmorValues(slot))))));
						DebugLib.outputFormattedDebug("Armor Values for slot %s: %s", slot.getName(), this.armorVals.get(slot));
					});
				});
			}
		});
	}

	@Override
	public void handleDamageStage(LivingDamageEvent evt) {
		this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			DDDCombatCalculations.classifyDefenses(this.ctx).ifPresent((defenses) -> {
				DDDEnchantmentInfo enchants = DDDCombatCalculations.getDDDEnchants(this.ctx);
				if(!enchants.isSlyStrike() && defenses.immunities.stream().map(Functions.compose((f) -> f != null && f > 0.0f, m::remove)).reduce(Boolean::logicalOr).orElse(false)) {
					this.results.hasImmunity();
				}
				defenses.resistances.forEach((k, v) -> {
					float resist = v > 0 ? MathHelper.clamp(v - enchants.getBruteForce(), 0.0f, 1.0f) : v;
					if(m.computeIfPresent(k, (type, dmg) -> dmg * Math.max(1 - resist, 0)) != null) {
						if(resist > 0) {
							this.results.hasResistance();
						}
						else if (resist < 0 && m.get(k) > 0) {
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
							//$FALL-THROUGH$
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
	public void clear() {
		IDDDCombatTracker.super.clear();
	}
	
	private void updateContextAndDamage(DamageSource src, float amount, @Nullable Entity attacker) {
		DistinctDamageDescriptions.debug("Updating context and damage...");
		if(this.ctx == null) {
			DistinctDamageDescriptions.debug("Setting new context...");
			this.setCombatContext(new CombatContext(src, amount, attacker, this.getFighter()));
			this.results.withStartingDamage(amount);
			this.damage = amount;
		}
		else if(this.incomingDamage != null && amount != this.damage && this.ctx.contextMatches(src, attacker)) {
			DebugLib.doDebug(() -> DebugLib.outputFormattedDebug("Adjusting damage... Current: %f, New %f", this.damage, amount));
			this.incomingDamage.distributeDamageToCurrentTypes(amount);
		}
	}

}
