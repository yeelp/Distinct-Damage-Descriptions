package yeelp.distinctdamagedescriptions.api;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.util.IDamageCategories;

public interface IDistinctDamageDescriptionsAccessor
{
	/**
	 * Get the damage categories for an ItemStack
	 * @param stack
	 * @return IDamageCategories, or null if the stack doesn't have it.
	 */
	@Nullable
	IDamageCategories getDamageCategories(ItemStack stack);
	
	/**
	 * Get an entity's slashing resistance
	 * @param entity
	 * @return that entity's slashing resistance.
	 */
	double getSlashingResistance(EntityLivingBase entity);
	
	/**
	 * Get an entity's piercing resistance
	 * @param entity
	 * @return that entity's piercing resistance
	 */
	double getPiercingResistance(EntityLivingBase entity);
	/**
	 * Get an entity's bludgeoning resistance
	 * @param entity
	 * @return that entity's bludgeoning resistance.
	 */
	double getBludgeoningResistance(EntityLivingBase entity);
}
