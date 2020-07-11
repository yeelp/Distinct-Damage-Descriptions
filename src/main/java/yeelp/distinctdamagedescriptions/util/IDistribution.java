package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public abstract interface IDistribution extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Get the slashing weight
	 * @return float in [0,1] for percent of slashing weight
	 */
	float getSlashingWeight();
	
	/**
	 * Get the piercing weight
	 * @return float in [0,1] for percent of piercing weight
	 */
	float getPiercingWeight();
	
	/**
	 * Get the bludgeoning weight
	 * @return float in [0,1] for percent of bludgeoning weight
	 */
	float getBludgeoningWeight();
	
	/**
	 * Set new damage distribution weights
	 * @param slash new slashing weight
	 * @param pierce new piercing weight
	 * @param bludegoning new bludgeoning weight
	 * @throws InvariantViolationException
	 */
	void setNewWeights(float slash, float pierce, float bludgeoning) throws InvariantViolationException;
}
