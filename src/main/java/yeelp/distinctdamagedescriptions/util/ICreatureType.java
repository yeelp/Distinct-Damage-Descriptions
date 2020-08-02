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
	
	/**
	 * Get the modifier for a damage type.
	 * @param damageTypeName name
	 * @return a float modifier from both creature types if present.
	 */
	default float getModifierForDamageType(String damageTypeName)
	{
		CreatureTypeData sub = getSubCreatureTypeData();
		if(sub == CreatureTypeData.UNKNOWN)
		{
			return getMainCreatureTypeData().getModifierForDamageType(damageTypeName);
		}
		else
		{
			return 0.75f*getMainCreatureTypeData().getModifierForDamageType(damageTypeName) + 0.25f*sub.getModifierForDamageType(damageTypeName);
		}
	}
}
