package yeelp.distinctdamagedescriptions.api;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;

public interface IDDDResistanceModifier extends IDDDCapModifier<EntityLivingBase> {

	/**
	 * Get the new adaptability amount to add. A negative amount will of course
	 * subtract.
	 * 
	 * @return The adaptability amount modifier.
	 */
	float getAdaptabilityMod();

	/**
	 * Set the adaptability mod.
	 * 
	 * @param amount the value to set.
	 */
	void setAdaptabilityMod(float amount);

	/**
	 * Get if the adatability status should be toggled either on or off. A return
	 * value of {@link Optional#empty()} indicates the adaptability status should
	 * not be altered.
	 * 
	 * @return The new adaptability status or an empty Optional if it should not be
	 *         changed.
	 */
	Optional<Boolean> toggleAdaptability();

	/**
	 * Set the state of adaptability for this modifier
	 * 
	 * @param state the state to set. True will enable adaptability, false will
	 *              disable, and null will leave it unchanged.
	 */
	void setAdaptability(Boolean state);

	/**
	 * Get the set of damage types to add as immunities.
	 * 
	 * @return The set of damage type immunities to add.
	 */
	Set<DDDDamageType> getImmunitiesToAdd();

	/**
	 * Get the set of damage types to remove as immunities, should they have it.
	 * 
	 * @return The set of damage type immunities to remove.
	 */
	Set<DDDDamageType> getImmunitiesToRemove();

	/**
	 * Add a type as an immunity
	 * 
	 * @param type The type to add.
	 */
	void addImmunity(DDDDamageType type);

	/**
	 * Remove a type added as an immunity. Not the same as removing an immunity from
	 * the target.
	 * 
	 * @param type the type to remove,
	 * 
	 * @see #removeImmunity(DDDDamageType)
	 */
	void removeAddedImmunity(DDDDamageType type);

	/**
	 * Add a type to be removed as an immunity.
	 * 
	 * @param type type to remove as an immunity.
	 */
	void removeImmunity(DDDDamageType type);

	/**
	 * Remove a type from the set of immunities to be removed. Not the same as
	 * specifying a type to be removed as an immunity, this actually removes the
	 * type from being removed.
	 * 
	 * @param type The type to remove.
	 * 
	 * @see #removeImmunity(DDDDamageType)
	 */
	void removeRemovedImmunity(DDDDamageType type);
}
