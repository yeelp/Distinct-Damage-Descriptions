package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorModifierInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IValidArmorSlotInjector;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DDDCombatCalculations {

	private static Random rand = new Random();
	private static final IClassifier<DamageMap> DAMAGE_CLASSIFIER = new DamageClassifier();
	private static final IClassifier<ArmorClassification> ARMOR_CLASSIFIER = new ArmorClassifier();
	private static final IClassifier<MobDefenses> DEFENSES_CLASSIFIER = new DefensesClassifier();
	
	private static final Set<IArmorModifierInjector> ARMOR_MOD_INJECTORS = Sets.newTreeSet();
	
	public static final void registerArmorValuesInjector(IArmorValuesInjector injector) {
		ArmorClassifier.registerInjector(injector);
	}
	
	public static final void registerValidArmorSlotInjector(IValidArmorSlotInjector injector) {
		CombatContext.registerInjector(injector);
	}
	
	public static final void registerArmorModifierInjector(IArmorModifierInjector injector) {
		ARMOR_MOD_INJECTORS.add(injector);
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
		if(evt.getEntityLiving().world.isRemote) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> ct.handleAttackStage(evt));
		DeveloperModeKernel.onAttackCallback(evt);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static final void onHurt(LivingHurtEvent evt) {
		if(evt.getEntityLiving().world.isRemote) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> {
			ct.handleHurtStage(evt);
			ct.getIncomingDamage().map(Functions.compose(YMath::sum, DamageMap::values)).filter((d) -> !Double.isNaN(d)).map(Double::floatValue).ifPresent((f) -> {
				evt.setAmount(f);
				ct.setDamageReference(f);
			});
			if(!ModConfig.resist.enableArmorCalcs) {
				return;
			}
			ct.getNewDeltaArmorValues().ifPresent((vals) -> {
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
					applyNewArmorModifier(ct.getFighter(), DDDAttributeModifierCollections.ARMOR, newAVals.getArmor());
					applyNewArmorModifier(ct.getFighter(), DDDAttributeModifierCollections.TOUGHNESS, newAVals.getToughness());
				});					
			});
		});
		DeveloperModeKernel.onHurtCallback(evt);
	}

	// Highest priority to remove temporary attribute modifiers.
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static final void onDamage(LivingDamageEvent evt) {
		if(evt.getEntityLiving().world.isRemote) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((ct) -> {
			if(ModConfig.resist.enableArmorCalcs) {	
				removeArmorModifier(ct.getFighter(), DDDAttributeModifierCollections.ARMOR);
				removeArmorModifier(ct.getFighter(), DDDAttributeModifierCollections.TOUGHNESS);
			}
			ct.handleDamageStage(evt);
			ct.getRecentResults().getAmount().ifPresent((d) -> evt.setAmount((float) d));
			DDDEffects.doEffects(evt.getSource().getTrueSource(), ct.getFighter(), ct.getRecentResults());
		});
		DeveloperModeKernel.onDamageCallback(evt);
	}

	@SubscribeEvent
	public static final void onKnockback(LivingKnockBackEvent evt) {
		if(evt.getEntityLiving().world.isRemote) {
			return;
		}
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).map(IDDDCombatTracker::getRecentResults).map(Predicates.or(Predicates.and(CombatResults::wasImmunityTriggered, (results) -> results.getAmount().orElse(Double.NaN) == 0), CombatResults::wasShieldEffective)::test).ifPresent(evt::setCanceled);
	}

	private static void removeArmorModifier(EntityLivingBase entity, DDDAttributeModifierCollections.ModifierRecord modifierType) {
		entity.getAttributeMap().getAttributeInstance(modifierType.getAttributeModified()).removeModifier(modifierType.getUUID());
	}

	private static void applyNewArmorModifier(EntityLivingBase entity, DDDAttributeModifierCollections.ModifierRecord modifierType, float delta) {
		entity.getAttributeMap().getAttributeInstance(modifierType.getAttributeModified()).applyModifier(new AttributeModifier(modifierType.getUUID(), modifierType.getName(), delta, 0));
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
}
