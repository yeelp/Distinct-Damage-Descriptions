package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;

/**
 * Our hacky solution for items that need their distribution updated after creation.
 * @author Yeelp
 */
public interface IDistributionRequiresUpdate extends ICapabilitySerializable<NBTTagByte> {

	/**
	 * If this distribution has been updated on the stack correctly
	 * @return true if updated.
	 */
	boolean updated();
	
	/**
	 * Mark this cap as needing to recalculate
	 */
	void markForUpdate();
	
	/**
	 * Get the updated distribution to apply to the ItemStack
	 * @param stack the stack to calculate the new distribution for.
	 * @return The updated distribution map
	 */
	Map<DDDDamageType, Float> getUpdatedDistribution(ItemStack stack);
	
	/**
	 * Apply the updated distribution to the ItemStack
	 * @param stack the stack to apply it to.
	 */
	void applyDistributionToStack(ItemStack stack);
}
