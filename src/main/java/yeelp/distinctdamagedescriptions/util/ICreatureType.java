package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ICreatureType extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Get the main creature type
	 * @return the main CreatureTypeData
	 */
	CreatureTypeData getMainCreatureTypeData();
	
	/**
	 * Get the sub creature type
	 * @return the sub CreatureTypeData
	 */
	CreatureTypeData getSubCreatureTypeData();
	
	/**
	 * Is this creature type immune to a PotionEffect?
	 * @param effect the Potion effect
	 * @return true if immune, false if not.
	 */
	boolean isImmuneToPotionEffect(PotionEffect effect);

	/**
	 * Is this creature type immune to critical hits?
	 * @return true if immune, false if not.
	 */
	boolean isImmuneToCriticalHits();
}
