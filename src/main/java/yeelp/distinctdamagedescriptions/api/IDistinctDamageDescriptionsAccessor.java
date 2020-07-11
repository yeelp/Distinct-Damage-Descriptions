package yeelp.distinctdamagedescriptions.api;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageType;
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
	 * @return the IMobResistances for that entity
	 */
	IMobResistances getMobResistances(EntityLivingBase entity);
	
	/**
	 * Get a map of armor resistance for an entity
	 * @param entity
	 * @return a Map that maps equipment slots to specific IArmorReistances present in that slot
	 */
	Map<EntityEquipmentSlot, IArmorResistances> getArmorResistancesForEntity(EntityLivingBase entity);
	
	/**
	 * Get a map of damage types to armor values for that damage type.
	 * @param entity
	 * @return A Map mapping damage types to a tuple (armor, toughness).
	 */
	Map<DamageType, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity);
	
	/**
	 * classify and categorize damage.
	 * @param resistances the attacked mob's IMobResistances capability
	 * @param src DamageSource 
	 * @param damage total damage dealt
	 * @return a map that categorizes damage based on damage type. A mob with immunities will have those damage types removed from the map; this map contains only entries that do damage.
	 */
	Map<DamageType, Float> classifyDamage(@Nonnull IMobResistances resistances, @Nonnull DamageSource src, float damage);
}
