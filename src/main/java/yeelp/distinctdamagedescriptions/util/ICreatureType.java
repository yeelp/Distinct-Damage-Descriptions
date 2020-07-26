package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
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
}
