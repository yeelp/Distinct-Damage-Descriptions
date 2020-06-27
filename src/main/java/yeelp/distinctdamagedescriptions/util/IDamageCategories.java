package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * The individual damage categories for a specified item stack
 * @author Yeelp
 *
 */
public interface IDamageCategories extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Get the total damage inflicted
	 * @return the total damage
	 */
	default float getTotalDamage()
	{
		return getDamage(DamageType.BLUDGEONING) + getDamage(DamageType.PIERCING) + getDamage(DamageType.SLASHING);
	}
	/**
	 * Get the damage inflicted of a certain type.
	 * @param type DamageType enum.
	 * @return the amount of damage inflicted of that type.
	 */
	float getDamage(DamageType type);
	/**
	 * Set the amount of damage to inflict of a certain type
	 * @param type DamageType enum
	 * @param amount amount of damage to do of that type.
	 */
	void setDamage(DamageType type, float amount);
}
