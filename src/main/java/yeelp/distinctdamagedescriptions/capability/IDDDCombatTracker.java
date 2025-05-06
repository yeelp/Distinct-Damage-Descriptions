package yeelp.distinctdamagedescriptions.capability;

import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.capability.impl.DDDCombatTracker;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DamageCalculation;

/**
 * A combat tracker capability for handling Entity specific damage calculations.
 * 
 * @author Yeelp
 *
 */
public interface IDDDCombatTracker extends DDDCapabilityBase<NBTTagCompound> {

	EntityLivingBase getFighter();

	/**
	 * Get a map of the incoming damage. Classifies damage if needed.
	 * 
	 * @return An {@link Optional} wrapping the incoming damage map or an empty
	 *         Optional if there is no such map.
	 */
	Optional<DamageMap> getIncomingDamage();

	/**
	 * Get the current {@link DamageCalculation} in progress
	 * 
	 * @return The current damage calculation
	 */
	Optional<DamageCalculation> getCurrentCalculation();

	/**
	 * Get the last {@link DamageCalculation} that this tracker produced, but only
	 * if the calculation was finished no longer than
	 * {@code noLongerValidAfterTicks} ticks ago. This is used for any effects that
	 * should apply as a result of the damage calculation, but are applied after the
	 * calculation was popped from the stack.
	 * 
	 * @param noLongerValidAfterTicks The time in ticks in which this calculation should not be retrieved.
	 * @return The last damage calculation, or and empty {@link Optional} if there
	 *         was no last calculation or if it is no longer valid.
	 */
	Optional<DamageCalculation> getLastCalculation(int noLongerValidAfterTicks);

	/**
	 * Get the last {@link CombatResults} that DDD produced from the last damage
	 * calculation.
	 * 
	 * @return The most recent combat results.
	 */
	CombatResults getRecentResults();

	/**
	 * Apply new attribute modifiers for armor and toughness. Existing modifiers
	 * will be overwritten, but saved until {@link #removeArmorModifiers()} is
	 * called, whereupon the old values will be restored.
	 * 
	 * @param delta An ArmorValues container describing the <em>change</em> in armor
	 *              and toughness that should be applied.
	 */
	void applyArmorModifier(ArmorValues delta);

	/**
	 * Remove the armor and toughness attribute modifiers. Old armor and toughness
	 * attribute modifiers applied via {@link #applyArmorModifier(ArmorValues)} will
	 * be restored.
	 */
	void removeArmorModifiers();

	/**
	 * Handle the Attack stage of damage.
	 * 
	 * @param evt The LivingAttackEvent.
	 */
	void handleAttackStage(LivingAttackEvent evt);

	/**
	 * Handle the Hurt stage of damage.
	 * 
	 * @param evt The LivingHurtEvent.
	 */
	void handleHurtStage(LivingHurtEvent evt);

	/**
	 * Handle the Damage stage of damage.
	 * 
	 * @param evt The LivingDamageEvent.
	 */
	void handleDamageStage(LivingDamageEvent evt);
	
	/**
	 * Clear the tracker
	 */
	void clear();

	static void register() {
		DDDCapabilityBase.register(IDDDCombatTracker.class, NBTTagCompound.class, () -> new DDDCombatTracker(null));
	}
}
