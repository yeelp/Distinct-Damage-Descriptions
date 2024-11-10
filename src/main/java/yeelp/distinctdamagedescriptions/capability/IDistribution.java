package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public abstract interface IDistribution extends DDDUpdatableCapabilityBase<NBTTagCompound> {
	/**
	 * Get the weight associated with a certain type
	 * 
	 * @param type
	 * @return the weight of that type in this distribution.
	 */
	float getWeight(DDDDamageType type);

	/**
	 * Get the base weight associated with a certain type before modifications.
	 * 
	 * @param type
	 * @return the weight of that type in this distribution.
	 */
	float getBaseWeight(DDDDamageType type);

	/**
	 * Set new weight
	 * 
	 * @param type
	 * @param amount
	 */
	void setWeight(DDDDamageType type, float amount);

	/**
	 * Set the base weight. This persists across calls to {@link #update(ItemStack)}
	 * 
	 * @param type
	 * @param amount
	 */
	void setBaseWeight(DDDDamageType type, float amount);

	/**
	 * Set new distribution weights. Will overwrite existing weights and add new
	 * ones.
	 * 
	 * @param map a map containing new weights
	 * @throws InvariantViolationException if a weight < 0.
	 */
	void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException;

	/**
	 * Set new distribution base weights. Will overwrite existing weights and add
	 * new ones.
	 * 
	 * @param map a map containing new weights
	 * @throws InvariantViolationException if a weight < 0.
	 */
	void setNewBaseWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException;

	/**
	 * Get the categories for this distribution.
	 * 
	 * @return A set containing all the types in this distribution.
	 */
	Set<DDDDamageType> getCategories();

	/**
	 * Get the categories for this distribution before modifications.
	 * 
	 * @return A set containing all the types this distribution started with.
	 */
	Set<DDDDamageType> getBaseCategories();

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
