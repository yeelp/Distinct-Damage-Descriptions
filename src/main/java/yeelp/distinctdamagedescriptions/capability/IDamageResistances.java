package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

/**
 * Damage Resistances capability base.
 * 
 * @author Yeelp
 *
 */
public interface IDamageResistances extends ISyncableCapability<NBTTagCompound> {

	/**
	 * Get resistance for a certain type.
	 * 
	 * @param type
	 * @return the resistance for that type.
	 */
	float getResistance(DDDDamageType type);

	/**
	 * Set the resistance for a certain type.
	 * 
	 * @param type
	 * @param value the resistance value to set.
	 */
	void setResistance(DDDDamageType type, float value);

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
	 * Get immunity status for a type
	 * 
	 * @param type
	 * @return true if immune, false otherwise.
	 */
	boolean hasImmunity(DDDDamageType type);

	/**
	 * Set immunity status for a certain type.
	 * 
	 * @param type
	 * @param status true if immune, false if susceptible
	 */
	void setImmunity(DDDDamageType type, boolean status);

	/**
	 * Clear all immunities
	 */
	void clearImmunities();

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

	default ResistMap getAllResistances() {
		ResistMap rMap = DDDMaps.newResistMap();
		DDDRegistries.damageTypes.getAll().forEach((t) -> rMap.put(t, this.getResistance(t)));
		return rMap;
	}

}
