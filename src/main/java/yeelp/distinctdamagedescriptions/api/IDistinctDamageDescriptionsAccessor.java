package yeelp.distinctdamagedescriptions.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.*;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public interface IDistinctDamageDescriptionsAccessor {
	/**
	 * Get the damage distribution for an ItemStack - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param stack item stack to get distribution from. Can be null.
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
	@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull ItemStack stack) {
		return this.getDamageDistribution(Objects.requireNonNull(stack, "Null stack can't have Damage Distribution!")).get();
	}

	/**
	 * Get the damage distribution for an EntityLivingBase - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param entity Entity to get damage distribution from.
	 * @return the IDamageDistribution capability for this EntityLivingBase.
	 */
	Optional<IDamageDistribution> getDamageDistribution(@Nullable EntityLivingBase entity);

	/**
	 * Get the damage distribution of an entity if it is known to exist
	 * 
	 * @param entity target entity. Can't be null
	 * @return the damage distribution of the entity.
	 */
	@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull EntityLivingBase entity) {
		return this.getDamageDistribution(Objects.requireNonNull(entity, "Null entity can't have damage distribution!")).get();
	}

	/**
	 * Get the damage distribution for an IProjectile - an instance of
	 * {@link IDamageDistribution}
	 * 
	 * @param projectile projectile to get damage distribution from.
	 * @return the IDamageDistribution capability for this IProjectile.
	 */
	Optional<IDamageDistribution> getDamageDistribution(@Nullable IProjectile projectile);

	/**
	 * Get the damage distribution for a projectile if it is known to exist
	 * 
	 * @param projectile projectile. Can't be null
	 * @return the damage distribution of the projectile.
	 */
	@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
	default IDamageDistribution getDamageDistributionUnsafe(@Nonnull IProjectile projectile) {
		return this.getDamageDistribution(Objects.requireNonNull(projectile, "Null projectile can't have damage distribution!")).get();
	}

	/**
	 * Get the armor resistances for an ItemStack - an instance of
	 * {@link IArmorDistribution}
	 * 
	 * @param stack item stack. May be null.
	 * @return the IArmorResistances capability for this ItemStack, or an empty
	 *         Optional if it doesn't have it.
	 */
	Optional<IArmorDistribution> getArmorResistances(@Nullable ItemStack stack);

	/**
	 * Get the mob resistances for an EntityLivingBase - an instance of
	 * {@link IMobResistances}
	 * 
	 * @param entity entity to get resistances from. May be null.
	 * @return the IMobResistances for that entity
	 */
	Optional<IMobResistances> getMobResistances(@Nullable EntityLivingBase entity);

	/**
	 * Get the mob's creature type - an instance of {@link IMobCreatureType}
	 * 
	 * @param entity entity to get creature types from. May be null.
	 * @return the IMobCreatureType the mob has. Always returns
	 *         {@link MobCreatureType.UNKNOWN} for mobs that are instances of
	 *         EntityPlayer
	 */
    @SuppressWarnings("JavadocReference")
    Optional<IMobCreatureType> getMobCreatureType(@Nullable EntityLivingBase entity);

	/**
	 * Get a stack's shield distribution
	 * 
	 * @param stack stack to get shield distribution from.
	 * @return a ShieldDistribution, or null if the stack doesn't have this
	 *         capability.
	 */
	Optional<ShieldDistribution> getShieldDistribution(@Nullable ItemStack stack);

	/**
	 * Get this entity's DDDCombatTracker. Wrapped in an {@link Optional} if it
	 * doesn't exist. It will almost always exist, it only won't exist if the
	 * capability is removed or the entity hasn't had capabilities added yet.
	 * 
	 * @param entity entity to get combat tracker from.
	 * @return The entity's DDDCombatTracker.
	 */
	Optional<IDDDCombatTracker> getDDDCombatTracker(EntityLivingBase entity);

}
