package yeelp.distinctdamagedescriptions.api;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

public abstract interface IDistinctDamageDescriptionsAccessor {
	/**
	 * Get the damage distribution for an ItemStack - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param stack
	 * @return the IDamageDistribution capability for this ItemStack, empty Optional
	 *         for an empty stack.
	 */
	Optional<IDamageDistribution> getDamageDistribution(@Nullable ItemStack stack);

	/**
	 * Get the damage distribution of an item, if it is known to exist
	 * 
	 * @param stack item stack. Can't be null
	 * @return The damage distribution of the stack
	 */
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull ItemStack stack) {
		return this.getDamageDistribution(Objects.requireNonNull(stack, "Null stack can't have Damage Distribution!")).get();
	}

	/**
	 * Get the damage distribution for an EntityLivingBase - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param entity
	 * @return the IDamageDistribution capability for this EntityLivingBase.
	 */
	Optional<IDamageDistribution> getDamageDistribution(@Nullable EntityLivingBase entity);

	/**
	 * Get the damage distribution of an entity if it is known to exist
	 * 
	 * @param entity target entity. Can't be null
	 * @return the damage distribution of the entity.
	 */
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull EntityLivingBase entity) {
		return this.getDamageDistribution(Objects.requireNonNull(entity, "Null entity can't have damage distribution!")).get();
	}

	/**
	 * Get the damage distribution for an IProjectile - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param projectile
	 * @return the IDamageDistribution capability for this IProjectile.
	 */
	Optional<IDamageDistribution> getDamageDistribution(@Nullable IProjectile projectile);

	/**
	 * Get the damage distribution for a projectile if it is known to exist
	 * 
	 * @param projectile projectile. Can't be null
	 * @return the damage distribution of the projectile.
	 */
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull IProjectile projectile) {
		return this.getDamageDistribution(Objects.requireNonNull(projectile, "Null projectile can't have damage distribution!")).get();
	}

	/**
	 * Get the armor resistances for an ItemStack - an instance of
	 * {@link IArmorDistribution}
	 * 
	 * @param stack
	 * @return the IArmorResistances capability for this ItemStack, or an empty
	 *         Optional if it doesn't have it.
	 */
	Optional<IArmorDistribution> getArmorResistances(@Nullable ItemStack stack);

	/**
	 * Get the mob resistances for an EntityLivingBase - an instance of
	 * {@link IMobResistances}
	 * 
	 * @param entity
	 * @return the IMobResistances for that entity
	 */
	Optional<IMobResistances> getMobResistances(@Nullable EntityLivingBase entity);

	/**
	 * Get the mob's creature type - an instance of {@link IMobCreatureType}
	 * 
	 * @param entity
	 * @return the IMobCreatureType the mob has. Always returns
	 *         {@link MobCreatureType.UNKNOWN} for mobs that are instances of
	 *         EntityPlayer
	 */
	Optional<IMobCreatureType> getMobCreatureType(@Nullable EntityLivingBase entity);

	/**
	 * Get a stack's shield distribution
	 * 
	 * @param stack
	 * @return a ShieldDistribution, or null if the stack doesn't have this
	 *         capability.
	 */
	Optional<ShieldDistribution> getShieldDistribution(@Nullable ItemStack stack);

	/**
	 * Get this entity's DDDCombatTracker. Wrapped in an {@link Optional} if it
	 * doesn't exist. It will almost always exist, it only won't exist if the
	 * capability is removed or the entity hasn't had capabilities added yet.
	 * 
	 * @param entity
	 * @return The entity's DDDCombatTracker.
	 */
	Optional<IDDDCombatTracker> getDDDCombatTracker(EntityLivingBase entity);

}
