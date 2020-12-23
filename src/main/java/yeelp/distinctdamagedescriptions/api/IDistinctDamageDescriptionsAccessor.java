package yeelp.distinctdamagedescriptions.api;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes;
import yeelp.distinctdamagedescriptions.util.CreatureType;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ICreatureType;
import yeelp.distinctdamagedescriptions.util.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.IMobResistances;
import yeelp.distinctdamagedescriptions.util.ShieldDistribution;

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
	 * Get the mob resistances for an EntityLivingBase - an instance of {@link IMobResistances}
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
	 * Get a stack's shield distribution
	 * @param stack
	 * @return a ShieldDistribution, or null if the stack doesn't have this capability.
	 */
	@Nullable
	ShieldDistribution getShieldDistribution(ItemStack stack);
	
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
	Map<String, Tuple<Float, Float>> getArmorValuesForEntity(EntityLivingBase entity, boolean bootsOnly, boolean helmetOnly);
	
	/**
	 * classify and categorize damage.
	 * @param src DamageSource
	 * @param damage total damage dealt
	 * @return a map that categorizes damage based on damage type. A mob with immunities will have those damage types removed from the map; this map contains only entries that do damage.
	 * null is returned if the damage source wasn't classified and categorized.
	 */
	@Nullable
	Map<String, Float> classifyDamage(@Nonnull DamageSource src, float damage);
	
	/**
	 * Divide resistances into categories
	 * @param types
	 * @param resists
	 * @return a map of relevant resistances. If a mob is immune to a damage type, {@link Float#MAX_VALUE} is put in the map.
	 */
	Map<String, Float> classifyResistances(Set<String> types, IMobResistances resists);
	
	/**
	 * Apply DDD modifications to a DamageSource
	 * @param src the src to apply modifications to.
	 * @return A new DamageSource; either a {@link DDDDamageType} if the DamageSource had additional context, or {@code src} if no additional context could be applied.
	 */
	DamageSource getDamageContext(DamageSource src);
	
	/**
	 * Check if a {@link DDDDamageType} is physical (slash, pierce, bludgeoning) only. 
	 * @param src
	 * @return true if only physical.
	 */
	boolean isPhysicalDamageOnly(DDDDamageType src);

	/**
	 * Check if a damage type string is physical.
	 * @param damageType
	 * @return true if physical, false if not.
	 */
	default boolean isPhysicalDamage(String damageType)
	{
    	return damageType.equals(InternalDamageTypes.BLUDGEONING) ||
    		   damageType.equals(InternalDamageTypes.PIERCING) ||
    		   damageType.equals(InternalDamageTypes.SLASHING);
    }
	
}
