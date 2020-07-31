package yeelp.distinctdamagedescriptions.api;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ICreatureType;
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
	 * Get the damage distribution for an IProjectile - an instance of {@link IDamageDistribution}
	 * @param projectile
	 * @return the IDamageDistribution capability for this IProjectile.
	 */
	IDamageDistribution getDamageDistribution(IProjectile projectile);
	
	/**
	 * Get the armor resistances for an ItemStack - an instance of {@link IArmorDistribution}
	 * @param stack
	 * @return the IArmorResistances capability for this ItemStack, or null if it doesn't have it.
	 */
	@Nullable
	IArmorDistribution getArmorResistances(ItemStack stack);
	
	/**
	 * Get the mob resistances for an ItemStack - an instance of {@link IMobResistances}
	 * @param entity
	 * @return the IMobResistances for that entity
	 */
	IMobResistances getMobResistances(EntityLivingBase entity);
	
	/**
	 * Get the mob's creature type - an instance of {@link ICreatureType}
	 * @param entity
	 * @return the ICreatureType the mob has. Always returns {@link CreatureType.UNKNOWN} for mobs that are instances of EntityPlayer
	 */
	ICreatureType getMobCreatureType(EntityLivingBase entity);
	
	/**
	 * Get a map of armor resistance for an entity
	 * @param entity
	 * @return a Map that maps equipment slots to specific IArmorReistances present in that slot
	 */
	Map<EntityEquipmentSlot, IArmorDistribution> getArmorDistributionsForEntity(EntityLivingBase entity);
	
	/**
	 * Get a map of damage types to armor values for that damage type.
	 * @param entity
	 * @param bootsOnly True if only boots should be used
	 * @param helmetOnly True if only helmets should be used
	 * @return A Map mapping damage types to a tuple (armor, toughness).
	 */
	Map<DamageType, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity, boolean bootsOnly, boolean helmetOnly);
	
	/**
	 * classify and categorize damage.
	 * @param resistances the attacked mob's IMobResistances capability
	 * @param src DamageSource 
	 * @param damage total damage dealt
	 * @return a map that categorizes damage based on damage type. A mob with immunities will have those damage types removed from the map; this map contains only entries that do damage.
	 * null is returned if the damage source wasn't classified and categorized.
	 */
	@Nullable
	Map<DamageType, Float> classifyDamage(@Nonnull IMobResistances resistances, @Nonnull DamageSource src, float damage);
}
