package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Damage Resistances capability base.
 * @author Yeelp
 *
 */
public interface IDamageResistances extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Get resistance for a certain type.
	 * @param type
	 * @return the resistance for that type.
	 */
	float getResistance(String type);
	
	/**
	 * Set the resistance for a certain type.
	 * @param type
	 * @param value the resistance value to set.
	 */
	void setResistance(String type, float value);
	
	/**
	 * Get immunity status for a type
	 * @param type
	 * @return true if immune, false otherwise.
	 */
	boolean hasImmunity(String type);
	
	/**
	 * Set immunity status for a certain type.
	 * @param type
	 * @param status true if immune, false if susceptible
	 */
	void setImmunity(String type, boolean status);
	
	/**
	 * Clear all immunities
	 */
	void clearImmunities();
	
}
