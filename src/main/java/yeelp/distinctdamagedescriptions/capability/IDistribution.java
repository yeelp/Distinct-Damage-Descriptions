package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public abstract interface IDistribution extends ICapabilitySerializable<NBTTagList>
{	
	/**
	 * Get the weight associated with a certain type
	 * @param type
	 * @return the weight of that type in this distribution.
	 */
	float getWeight(String type);
	
	/**
	 * 
	 * @param type
	 * @param amount
	 */
	void setWeight(String type, float amount);
	
	/**
	 * Set new damage distribution weights. Will overwrite existing weights and add new ones.
	 * @param map a map containing new weights
	 * @throws InvariantViolationException if a weight < 0.
	 */
	void setNewWeights(Map<String, Float> map) throws InvariantViolationException;
	
	/**
	 * Get the categories for this distribution.
	 * @return
	 */
	Set<String> getCategories();
}
