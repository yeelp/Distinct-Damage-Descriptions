package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Damage Distribution capability. <p>
 * INVARIANT: all weights will always add to 1.
 * @author Yeelp
 *
 */
public interface IDamageDistribution extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Distribute damage across all three categories
	 * @param dmg damage
	 * @return a DamageCategories with {@code dmg} distributed across all three categories
	 */
	DamageCategories distributeDamage(float dmg);
	
	/**
	 * Get the percent of damage that will be classified as slashing damage.
	 * @return float in [0,1] for percent of slashing damage
	 */
	float getSlashingWeight();
	
	/**
	 * Get the percent of damage that will be classified as piercing damage.
	 * @return float in [0,1] for percent of piercing damage
	 */
	float getPiercingWeight();
	
	/**
	 * Get the percent of damage that will be classified as bludgeoning damage.
	 * @return float in [0,1] for percent of bludgeoning damage
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
