package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.enchantments.EnchantmentBruteForce;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.util.DDDEffects;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.ArmorClassification;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorModifierInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.ICancelCalculationInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IValidArmorSlotInjector;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DDDCombatCalculations {

	private static Random rand = new Random();
	private static final IClassifier<DamageMap> DAMAGE_CLASSIFIER = new DamageClassifier();
	private static final IClassifier<ArmorClassification> ARMOR_CLASSIFIER = new ArmorClassifier();
	private static final IClassifier<MobDefenses> DEFENSES_CLASSIFIER = new DefensesClassifier();
	
	private static final Set<ICancelCalculationInjector> CANCEL_CALCULATION_INJECTORS = Sets.newTreeSet();
	private static final Set<IArmorModifierInjector> ARMOR_MOD_INJECTORS = Sets.newTreeSet();
	private static final ToDoubleFunction<DamageMap> SUM_DAMAGE = (m) -> YMath.sum(m.values());
	private static final int CLEAR_INTERVAL_TICKS = 20;
	
	public static final void registerArmorValuesInjector(IArmorValuesInjector injector) {
		ArmorClassifier.registerInjector(injector);
	}
	
	public static final void registerValidArmorSlotInjector(IValidArmorSlotInjector injector) {
		CombatContext.registerInjector(injector);
	}
	
	public static final void registerArmorModifierInjector(IArmorModifierInjector injector) {
		ARMOR_MOD_INJECTORS.add(injector);
	}
	
	public static final void registerCancelCalculationInjector(ICancelCalculationInjector injector) {
		CANCEL_CALCULATION_INJECTORS.add(injector);
	}
	
	public static Optional<DamageMap> classifyDamage(CombatContext ctx) {
		return DAMAGE_CLASSIFIER.classify(ctx);
	}

	public static Optional<ArmorClassification> classifyArmor(CombatContext ctx) {
		return ARMOR_CLASSIFIER.classify(ctx);
	}

	public static Optional<MobDefenses> classifyDefenses(CombatContext ctx) {
		return DEFENSES_CLASSIFIER.classify(ctx);
	}

	public static boolean doShieldCalcs(CombatContext ctx) {
		return ModConfig.resist.enableShieldCalcs && ctx.getShield().filter((stack) -> ModConfig.compat.definedItemsOnly || stack.hasCapability(ShieldDistribution.cap, null)).isPresent();
	}

	public static DDDDamageType getWeightedRandomRepresentativeType(DamageMap m) {
		float r = m.values().stream().reduce(Float::sum).get() * rand.nextFloat();
		Iterator<Entry<DDDDamageType, Float>> it = m.entrySet().stream().iterator();
		DDDDamageType type = DDDBuiltInDamageType.UNKNOWN;
		while(r > 0 && it.hasNext()) {
			Entry<DDDDamageType, Float> entry = it.next();
			type = entry.getKey();
			r -= entry.getValue();
		}
		DistinctDamageDescriptions.debug("Representative Type: "+type.getDisplayName());
		return type;
	}

	public static DDDEnchantmentInfo getDDDEnchants(CombatContext ctx) {
		if(ctx.getImmediateAttacker() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) ctx.getImmediateAttacker();
			return new DDDEnchantmentInfo(EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.slyStrike, attacker), EnchantmentHelper.getMaxEnchantmentLevel(DDDEnchantments.bruteForce, attacker));
		}
		return new DDDEnchantmentInfo();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static final void onAttack(LivingAttackEvent evt) {
		if(evt.getEntityLiving().world.isRemote || shouldCancel(ICancelCalculationInjector.determinePhase(evt), evt.getEntityLiving(), evt.getSource(), evt.getAmount())) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> ct.handleAttackStage(evt));
		DeveloperModeKernel.fireCallbacks(evt);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static final void onHurt(LivingHurtEvent evt) {
		if(evt.getEntityLiving().world.isRemote || shouldCancel(ICancelCalculationInjector.determinePhase(evt), evt.getEntityLiving(), evt.getSource(), evt.getAmount())) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> {
			ct.handleHurtStage(evt);
			DamageCalculation calc = ct.getCurrentCalculation().get();
			OptionalDouble potentialTotalDamage = mapToDouble(ct.getIncomingDamage(), SUM_DAMAGE);
			if(potentialTotalDamage.isPresent()) {
				float damage = (float) potentialTotalDamage.getAsDouble();
				evt.setAmount(damage);
				calc.setDamage(damage);
				if(damage <= 0.0f && ModConfig.compat.endEarlyCalculations) {
					DistinctDamageDescriptions.debug("Damage is zero in Hurt step, will leave calculation early.");
					//Leave early and don't apply armor mods if the damage is zero because Minecraft does the same.
					calc.markCompleted();
					ct.cleanUpAfterCalculation();
					return;
				}
			}
			if(!ModConfig.resist.enableArmorCalcs) {
				return;
			}
			calc.getDeltaArmor().ifPresent((vals) -> {
				boolean shouldApply = true;
				for(IArmorModifierInjector injector : ARMOR_MOD_INJECTORS) {
					if(shouldApply || injector.shouldFireIfNotBeingApplied()) {
						shouldApply = injector.modify(shouldApply, ct.getFighter(), vals);
					}
				}
				if(!shouldApply) {
					return;
				}
				vals.values().stream().reduce(ArmorValues::merge).ifPresent((newAVals) -> {
					ct.applyArmorModifier(newAVals);
				});					
			});
		});
		DeveloperModeKernel.fireCallbacks(evt);
	}

	// Highest priority to remove temporary attribute modifiers.
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static final void onDamage(LivingDamageEvent evt) {
		if(evt.getEntityLiving().world.isRemote || shouldCancel(ICancelCalculationInjector.determinePhase(evt), evt.getEntityLiving(), evt.getSource(), evt.getAmount())) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> {
			if(ModConfig.resist.enableArmorCalcs) {
				ct.removeArmorModifiers();
			}
			ct.handleDamageStage(evt);
			ct.getRecentResults().getAmount().ifPresent((d) -> evt.setAmount((float) d));
			DDDEffects.doEffects(evt.getSource().getTrueSource(), ct.getFighter(), ct.getRecentResults());
		});
		DeveloperModeKernel.fireCallbacks(evt);
	}

	@SubscribeEvent
	public static final void onKnockback(LivingKnockBackEvent evt) {
		if(evt.getEntityLiving().world.isRemote) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).map(IDDDCombatTracker::getRecentResults).map(Predicates.or(Predicates.and(CombatResults::wasImmunityTriggered, (results) -> results.getAmount().orElse(Double.NaN) == 0), CombatResults::wasShieldEffective)::test).ifPresent(evt::setCanceled);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static final void onEntityTick(LivingUpdateEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		if(entity.world.isRemote || evt.getEntityLiving().ticksExisted % CLEAR_INTERVAL_TICKS != 0) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(entity).ifPresent((ct) -> ct.getCurrentCalculation().ifPresent((calc) -> ct.clear()));
	}
	
	private static boolean shouldCancel(ICancelCalculationInjector.Phase phase, EntityLivingBase defender, DamageSource src, float amount) {
		boolean b;
		Iterator<ICancelCalculationInjector> it = CANCEL_CALCULATION_INJECTORS.iterator();
		for(b = false; it.hasNext(); b = it.next().shouldCancel(b, phase, defender, src, amount));
		return b;
	}
	
	private static <T> OptionalDouble mapToDouble(Optional<T> optional, ToDoubleFunction<T> function) {
		if(optional.isPresent()) {
			return OptionalDouble.of(function.applyAsDouble(optional.get()));
		}
		return OptionalDouble.empty();
	}

	public static final class DDDEnchantmentInfo {
		private final boolean slyStrike;
		private final float bruteForce;

		public DDDEnchantmentInfo() {
			this(false, 0);
		}

		private DDDEnchantmentInfo(boolean slyStrike, float bruteForce) {
			this.slyStrike = slyStrike;
			this.bruteForce = bruteForce;
		}

		public DDDEnchantmentInfo(int slyStrikeLevel, int bruteForceLevel) {
			this.slyStrike = slyStrikeLevel > 0;
			this.bruteForce = EnchantmentBruteForce.getBypassPerLevel(bruteForceLevel);
		}

		public boolean isSlyStrike() {
			return this.slyStrike;
		}

		public float getBruteForce() {
			return this.bruteForce;
		}
	}
	
	//public static final class 
}
