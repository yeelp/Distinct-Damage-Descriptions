package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public abstract interface IDistribution extends DDDCapabilityBase<NBTTagList> {
	/**
	 * Get the weight associated with a certain type
	 * 
	 * @param type
	 * @return the weight of that type in this distribution.
	 */
	float getWeight(DDDDamageType type);

	/**
	 * Set new weight
	 * 
	 * @param type
	 * @param amount
	 */
	void setWeight(DDDDamageType type, float amount);

	/**
	 * Set new damage distribution weights. Will overwrite existing weights and add
	 * new ones.
	 * 
	 * @param map a map containing new weights
	 * @throws InvariantViolationException if a weight < 0.
	 */
	void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException;

	/**
	 * Get the categories for this distribution.
	 * 
	 * @return
	 */
	Set<DDDDamageType> getCategories();

	/**
	 * Copy this instance
	 * 
	 * @return a copy
	 */
	IDistribution copy();

	/**
	 * Update this distribution with respect to it's ItemStack owner.
	 * 
	 * @param owner
	 * @return The mutated distribution, updated.
	 */
	IDistribution update(ItemStack owner);
}
