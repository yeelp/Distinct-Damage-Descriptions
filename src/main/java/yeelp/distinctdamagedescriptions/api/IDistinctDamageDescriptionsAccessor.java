package yeelp.distinctdamagedescriptions.api;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.util.IArmorResistances;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;

public abstract interface IDistinctDamageDescriptionsAccessor
{
	/**
	 * Get the damage distribution for an ItemStack - an instance of {@link IDamageDistribution}
	 * @param stack
	 * @return the IDamageDistribution capability for this ItemStack, null for an empty stack.
	 */
	@Nullable
	IDamageDistribution getDamageDistribution(ItemStack stack);
	
	/**
	 * Get the damage distribution for an EntityLivingBase - an instance of {@link IDamageDistribution}
	 * @param entity
	 * @return the IDamageDistribution capability for this EntityLivingBase.
	 */
	IDamageDistribution getDamageDistribution(EntityLivingBase entity);
	
	/**
	 * Get the armor resistances for an ItemStack - an instance of {@link IArmorResistances}
	 * @param stack
	 * @return the IArmorResistances capability for this ItemStack, or null if it doesn't have it.
	 */
	@Nullable
	IArmorResistances getArmorResistances(ItemStack stack);
	
	/**
	 * Get the mob resistances for an ItemStack - an instance of {@link IMobResistances}
	 * @param entity
	 * @return
	 */
	IMobResistances getMobResistances(EntityLivingBase entity);
	
	/**
	 * Get a map of armor resistance for an entity
	 * @param entity
	 * @return a Map that maps equipment slots to specific IArmorReistances present in that slot
	 */
	Map<EntityEquipmentSlot, IArmorResistances> getArmorResistancesForEntity(EntityLivingBase entity);
	
	/**
	 * Get the actual armor resistance values for an entity. This calls {@link #getArmorResistancesForEntity(EntityLivingBase)} then sums the values in the map accordingly.
	 * @param entity
	 * @return a float array containing {slashingResistance, piercingResistance, bludgeoningResistance, toughness} which are the total values provided by the armor worn by the entity.
	 */
	float[] getArmorResistanceValuesForEntity(EntityLivingBase entity);
}
