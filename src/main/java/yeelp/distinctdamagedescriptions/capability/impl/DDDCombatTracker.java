package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
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
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.DDDHooks;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.mixin.MixinASMEntityLivingBase;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatContext;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults.ResultsBuilder;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations.DDDEnchantmentInfo;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DamageCalculation;

public final class DDDCombatTracker implements IDDDCombatTracker {

	@CapabilityInject(IDDDCombatTracker.class)
	public static Capability<IDDDCombatTracker> cap;

	private final EntityLivingBase fighter;
	private CombatResults lastResults = CombatResults.NO_RESULTS;
	private DamageCalculation lastCalc = null;
	private int lastCalcTime;
	private Stack<ArmorValues> armorMods = new Stack<ArmorValues>();
	private Stack<DamageCalculation> calculations = new Stack<DamageCalculation>();

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
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// nothing to do
	}

	@Override
	public EntityLivingBase getFighter() {
		return this.fighter;
	}

	@Override
	public Optional<DamageMap> getIncomingDamage() {
		return this.getCurrentCalculation().map((calc) -> {
			if(!calc.wasDamageClassified()) {
				calc.classifyDamage();
			}
			return calc.getClassifiedDamage();
		});
	}

	@Override
	public Optional<DamageCalculation> getCurrentCalculation() {
		return this.calculations.isEmpty() ? Optional.empty() : Optional.of(this.calculations.peek());
	}

	@Override
	public Optional<DamageCalculation> getLastCalculation(int noLongerValidAfterTicks) {
		if(this.getFighter().ticksExisted - this.lastCalcTime <= noLongerValidAfterTicks) {
			return Optional.ofNullable(this.lastCalc);
		}
		return Optional.empty();
	}

	@Override
	public CombatResults getRecentResults() {
		return this.lastResults;
	}

	@Override
	public void applyArmorModifier(ArmorValues delta) {
		for(DDDAttributeModifierCollections.ArmorModifiers mod : DDDAttributeModifierCollections.ArmorModifiers.values()) {
			if(!this.armorMods.isEmpty()) {
				mod.removeModifier(this.getFighter());
			}
			mod.applyModifier(this.getFighter(), delta);
		}
		DistinctDamageDescriptions.debug("Pushing Armor Mods");
		this.armorMods.push(delta);
		this.getCurrentCalculation().get().markArmorModified();
	}

	@Override
	public void removeArmorModifiers() {
		this.removeArmorModifiers(false);
	}

	private void removeArmorModifiers(boolean force) {
		if(!force && !this.getCurrentCalculation().filter(DamageCalculation::wasArmorModified).isPresent()) {
			return;
		}
		if(!this.armorMods.isEmpty()) {
			this.armorMods.pop();
		}
		DistinctDamageDescriptions.debug("Removing Armor Mods");
		ArmorValues restoredValues = this.armorMods.isEmpty() ? null : this.armorMods.peek();
		for(DDDAttributeModifierCollections.ArmorModifiers mod : DDDAttributeModifierCollections.ArmorModifiers.values()) {
			mod.removeModifier(this.getFighter());
			if(restoredValues != null) {
				DistinctDamageDescriptions.debug("Applying previous armor mods");
				mod.applyModifier(this.getFighter(), restoredValues);
			}
		}
	}

	@Override
	public void handleAttackStage(LivingAttackEvent evt) {
		DamageCalculation calc = this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		CombatContext ctx = calc.getContext();
		if(this.getIncomingDamage().isPresent() && DDDCombatCalculations.doShieldCalcs(ctx)) {
			DamageMap dmg = calc.getClassifiedDamage();
			ItemStack shield = ctx.getShield().get();
			ShieldBlockEvent blockEvt = DDDHooks.fireShieldBlock(ctx.getImmediateAttacker(), ctx.getTrueAttacker(), this.getFighter(), ctx.getSource(), dmg, shield);
			if(!blockEvt.isCanceled() && Sets.intersection(blockEvt.getShieldDistribution().getCategories(), dmg.keySet()).size() > 0) {
				// record shield dist, but the distribution must be applied later. It won't work
				// otherwise.
				calc.setShieldDist(blockEvt.getShieldDistribution());
				if(ctx.getImmediateAttacker() instanceof EntityLivingBase) {
					((MixinASMEntityLivingBase) this.getFighter()).useBlockUsingShield((EntityLivingBase) ctx.getImmediateAttacker());
				}
			}
			this.getFighter().resetActiveHand();
		}
	}

	@Override
	public void handleHurtStage(LivingHurtEvent evt) {
		DamageCalculation calc = this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			CombatContext ctx = calc.getContext();
			calc.getShieldDist().ifPresent((shield) -> {
				double startingDamage = YMath.sum(m.values());
				shield.block(m);
				calc.getResultsBuilder().hasEffectiveShield(m);
				ctx.getShield().ifPresent((stack) -> stack.damageItem((int) Math.ceil(startingDamage * (calc.getResultsBuilder().build().getShieldRatio().getAsDouble())), this.getFighter()));
			});
			if(!m.isEmpty()) {
				calc.setType(DDDCombatCalculations.getWeightedRandomRepresentativeType(m));
			}
			if(ModConfig.resist.enableArmorCalcs) {
				DDDCombatCalculations.classifyArmor(ctx).ifPresent((classified) -> {
					calc.setArmorClassification(classified);
					Set<DDDDamageType> damagingTypes = m.keySet().stream().filter((t) -> m.get(t) > 0).collect(Collectors.toSet());
					DDDRegistries.damageTypes.getAll().stream().filter(Predicates.not(damagingTypes::contains)).forEach((type) -> {
						classified.forEachArmorMap((slot, map) -> map.remove(type));
					});
					//add missing types to the armor map as zero so values.stream() recognizes missing types as no effectiveness.
					damagingTypes.forEach((t) -> {
						classified.forEachArmorMap((slot, map) -> {
							if(!map.containsKey(t)) {
								map.put(t, new ArmorValues());
							}
						});
					});
					calc.setNewArmorValuesMap();
					Map<EntityEquipmentSlot, ArmorValues> armorVals = calc.getDeltaArmor().get();
					classified.forEachArmorMap((slot, map) -> {
						DebugLib.outputFormattedDebug("Armor Map for slot %s: %s", slot.toString(), DebugLib.entriesToString(map));
						armorVals.put(slot, ModConfig.resist.armorCalcRule.merge(map.values().stream().map((av) -> av.sub(ModConfig.resist.negativeRule.handlePotentialNegativeArmorValues(classified.getOriginalArmorValues(slot))))));
						DebugLib.outputFormattedDebug("Armor Values for slot %s: %s", slot.getName(), armorVals.get(slot));
					});
				});
			}
		});
	}

	@Override
	public void handleDamageStage(LivingDamageEvent evt) {
		DamageCalculation calc = this.updateContextAndDamage(evt.getSource(), evt.getAmount(), evt.getSource().getImmediateSource());
		this.getIncomingDamage().filter(Predicates.not(Map::isEmpty)).ifPresent((m) -> {
			CombatContext ctx = calc.getContext();
			ResultsBuilder results = calc.getResultsBuilder();
			DDDCombatCalculations.classifyDefenses(ctx).ifPresent((defenses) -> {
				DDDEnchantmentInfo enchants = DDDCombatCalculations.getDDDEnchants(ctx);
				if(!enchants.isSlyStrike() && defenses.immunities.stream().map(Functions.compose((f) -> f != null && f > 0.0f, m::remove)).reduce(Boolean::logicalOr).orElse(false)) {
					results.hasImmunity();
				}
				defenses.resistances.forEach((k, v) -> {
					float resist = v > 0 ? MathHelper.clamp(v - enchants.getBruteForce(), 0.0f, 1.0f) : v;
					if(m.computeIfPresent(k, (type, dmg) -> dmg * Math.max(1 - resist, 0)) != null) {
						if(resist > 0) {
							results.hasResistance();
						}
						else if(resist < 0 && m.get(k) > 0) {
							results.hasWeakness();
						}
					}
				});
				DDDAPI.accessor.getMobResistances(this.getFighter()).ifPresent((resistances) -> {
					UpdateAdaptiveResistanceEvent updateEvt = DDDHooks.fireUpdateAdaptiveResistances(ctx.getImmediateAttacker(), ctx.getTrueAttacker(), this.getFighter(), ctx.getSource(), m, defenses.resistances, defenses.immunities);
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
								results.wasAdaptive();
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
			results.withAmount((float) YMath.sum(m.values()));
		});
		calc.markCompleted();
		this.cleanUpAfterCalculation();
	}

	private void cleanUpAfterCalculation() {
		if(this.calculations.size() == 0) {
			return;
		}
		DamageCalculation calc = this.calculations.pop();
		if(calc.wasCompleted()) {
			this.lastResults = calc.getResultsBuilder().build();
			this.lastCalc = calc;
		}
		int armorMods = this.armorMods.size(), delta = armorMods - this.calculations.size();
		if(delta > 0) {
			if(ModConfig.salvageThis) {
				DistinctDamageDescriptions.err(String.format("Combat Tracker for %s has %d extra armor modifiers! They should always have the same amount! Salvaging requested, removing extras...", this.getFighter().getName(), delta));
				do {
					this.removeArmorModifiers(true);
				} while(this.armorMods.size() > this.calculations.size());				
			}
			else {
				throw new RuntimeException(String.format("Combat Tracker for %s has extra armor modifiers! This shouldn't happen! Mods: %s", this.getFighter().getName(), this.armorMods.toString()));
			}
		}
	}
	
	@Override
	public void clear() {
		this.calculations.clear();
		this.armorMods.clear();
		this.removeArmorModifiers(true);
	}

	private DamageCalculation updateContextAndDamage(DamageSource src, float amount, @Nullable Entity attacker) {
		DistinctDamageDescriptions.debug("Updating context and damage...");
		DamageCalculation calc;
		if(this.calculations.isEmpty()) {
			DistinctDamageDescriptions.debug("Setting new context...");
			calc = this.pushNewDamageCalculation(src, amount, attacker);
		}
		else if((calc = this.calculations.peek()).getContext().contextMatches(src, attacker)) {
			if(calc.getClassifiedDamage() != null && amount != calc.getDamage()) {
				float oldDamage = calc.getDamage();
				DebugLib.doDebug(() -> DebugLib.outputFormattedDebug("Adjusting damage... Current: %f, New %f", oldDamage, amount));
				calc.getClassifiedDamage().distributeDamageToCurrentTypes(amount);
				calc.setDamage(amount);
			}
		}
		else {
			DistinctDamageDescriptions.debug("Creating new nested damage calculation");
			calc = this.pushNewDamageCalculation(src, amount, attacker);
		}
		return calc;
	}

	private DamageCalculation pushNewDamageCalculation(DamageSource src, float amount, @Nullable Entity attacker) {
		DamageCalculation calc = new DamageCalculation(new CombatContext(src, amount, attacker, this.getFighter()));
		this.calculations.push(calc);
		calc.getResultsBuilder().withStartingDamage(amount);
		calc.setDamage(amount);
		this.lastCalcTime = this.getFighter().ticksExisted;
		return calc;
	}

}
