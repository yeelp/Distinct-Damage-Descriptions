package yeelp.distinctdamagedescriptions.api;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatTracker;

public abstract interface IDistinctDamageDescriptionsAccessor {
	/**
	 * Get the damage distribution for an ItemStack - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param stack
	 * @return the IDamageDistribution capability for this ItemStack, null for an
	 *         empty stack.
	 */
	@Nullable
	IDamageDistribution getDamageDistribution(ItemStack stack);

	/**
	 * Get the damage distribution for an EntityLivingBase - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param entity
	 * @return the IDamageDistribution capability for this EntityLivingBase.
	 */
	@Nullable
	IDamageDistribution getDamageDistribution(EntityLivingBase entity);

	/**
	 * Get the damage distribution for an IProjectile - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param projectile
	 * @return the IDamageDistribution capability for this IProjectile.
	 */
	@Nullable
	IDamageDistribution getDamageDistribution(IProjectile projectile);

	/**
	 * Get the armor resistances for an ItemStack - an instance of
	 * {@link IArmorDistribution}
	 * 
	 * @param stack
	 * @return the IArmorResistances capability for this ItemStack, or null if it
	 *         doesn't have it.
	 */
	@Nullable
	IArmorDistribution getArmorResistances(ItemStack stack);

	/**
	 * Get the mob resistances for an EntityLivingBase - an instance of
	 * {@link IMobResistances}
	 * 
	 * @param entity
	 * @return the IMobResistances for that entity
	 */
	@Nullable
	IMobResistances getMobResistances(EntityLivingBase entity);

	/**
	 * Get the mob's creature type - an instance of {@link ICreatureType}
	 * 
	 * @param entity
	 * @return the ICreatureType the mob has. Always returns
	 *         {@link CreatureType.UNKNOWN} for mobs that are instances of
	 *         EntityPlayer
	 */
	@Nullable
	ICreatureType getMobCreatureType(EntityLivingBase entity);

	/**
	 * Get a stack's shield distribution
	 * 
	 * @param stack
	 * @return a ShieldDistribution, or null if the stack doesn't have this
	 *         capability.
	 */
	@Nullable
	ShieldDistribution getShieldDistribution(ItemStack stack);
	
	/**
	 * Get this entity's DDDCombatTracker, if present. If not present, then this entity hasn't finised construction yet, or replaced their combat tracker
	 * @param entity
	 * @return The entity's DDDCombatTracker, if they have it.
	 */
	default Optional<DDDCombatTracker> getDDDCombatTracker(EntityLivingBase entity) {
		if(entity._combatTracker instanceof DDDCombatTracker) {
			return Optional.of((DDDCombatTracker) entity._combatTracker);
		}
		return Optional.empty();
	}

}
