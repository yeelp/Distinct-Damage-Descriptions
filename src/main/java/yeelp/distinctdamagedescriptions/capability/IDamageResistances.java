package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

/**
 * Damage Resistances capability base.
 * 
 * @author Yeelp
 *
 */
public interface IDamageResistances extends ISyncableCapability, DDDUpdatableCapabilityBase<NBTTagCompound> {

	/**
	 * Get resistance for a certain type.
	 * 
	 * @param type
	 * @return the resistance for that type.
	 */
	float getResistance(DDDDamageType type);

	/**
	 * Get resistance for a certain type before any modifications.
	 * 
	 * @param type
	 * @return The base resistance for that type.
	 */
	float getBaseResistance(DDDDamageType type);

	/**
	 * Set the resistance for a certain type. Only to be used by the application of
	 * {@link IDDDCapModifier} to set a modified resistance. Otherwise, any set
	 * resistance via this method will be reset by the next call to
	 * {@link #update(EntityLivingBase)}.
	 * 
	 * @param type
	 * @param value the resistance value to set.
	 */
	void setResistance(DDDDamageType type, float value);

	/**
	 * Set the base resistance for a certain type. Base resistance is the value
	 * before any modifiers are applied. Using
	 * {@link #setResistance(DDDDamageType, float)} will cause the new resistance to
	 * be reset to the base resistance once the capability calls
	 * {@link #update(EntityLivingBase)}.
	 * 
	 * @param type
	 * @param value the resistance value to set.
	 */
	void setBaseResistance(DDDDamageType type, float value);

	/**
	 * Removes the resistance for a certain type. This is similar, but not
	 * necessarily the same as {@code setResistance(type, 0.0f)}, as that sets the
	 * resistance to zero but the mob still technically "has" that resistance,
	 * whereas this method removes the resistance altogether.
	 * 
	 * @param type
	 */
	void removeResistance(DDDDamageType type);

	/**
	 * Removes the resistance for a certain type. This is similar, but not the same
	 * as {@code setBaseResistance(type, 0.0f)}, for the same reason as
	 * {@link #removeResistance(DDDDamageType)}. However, this applies to base
	 * resistances, not modified resistances.
	 * 
	 * @param type
	 */
	void removeBaseResistance(DDDDamageType type);

	/**
	 * Get immunity status for a type
	 * 
	 * @param type
	 * @return true if immune, false otherwise.
	 */
	boolean hasImmunity(DDDDamageType type);

	/**
	 * Get the immunity status for a type before any modifiers.
	 * 
	 * @param type
	 * @return true if immune baseline, false if not.
	 */
	boolean hasBaseImmunity(DDDDamageType type);

	/**
	 * Set immunity status for a certain type.
	 * 
	 * @param type
	 * @param status true if immune, false if susceptible
	 */
	void setImmunity(DDDDamageType type, boolean status);

	/**
	 * Set base immunity status for a certain type. Base immunity will not be
	 * removed with a call to {@link #update(EntityLivingBase)}.
	 * 
	 * @param type
	 * @param status true if immune, false if susceptible.
	 */
	void setBaseImmunity(DDDDamageType type, boolean status);

	/**
	 * Clear all immunities
	 */
	void clearImmunities();

	/**
	 * Clear all base immunities. {@link #clearImmunities()} can be thought of
	 * removing all active immunities for this instant, whereas this method removes
	 * all immunities this capability has before any modifiers.
	 */
	void clearBaseImmunities();

	/**
	 * Clear all resistances
	 */
	void clearResistances();

	/**
	 * Clear all base resistances; those that exist before modifiers.
	 */
	void clearBaseResistances();

	/**
	 * Copy this instance
	 * 
	 * @return a copy
	 */
	IDamageResistances copy();

	/**
	 * Update this instance of damage resistances with respect to its owner
	 * 
	 * @param owner The owner of this capability
	 * @return The updated copy. This mutates the original
	 */
	IDamageResistances update(EntityLivingBase owner);

	/**
	 * Get a copy of all resistances. Modifications to the returned
	 * {@link ResistMap} will not affect this capability.
	 * 
	 * @return A copy of all current resistances.
	 */
	default ResistMap getAllResistancesCopy() {
		ResistMap rMap = DDDMaps.newResistMap();
		DDDRegistries.damageTypes.getAll().forEach((t) -> rMap.put(t, this.getResistance(t)));
		return rMap;
	}

	/**
	 * Get a copy of all base resistances. Modifications to the returned
	 * {@link ResistMap} will not affect this capability.
	 * 
	 * @return A copy of all base resistances.
	 */
	default ResistMap getAllBaseResistancesCopy() {
		ResistMap rMap = DDDMaps.newResistMap();
		DDDRegistries.damageTypes.getAll().forEach((t) -> rMap.put(t, this.getBaseResistance(t)));
		return rMap;
	}

}
