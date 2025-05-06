package yeelp.distinctdamagedescriptions.api.impl.dists;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

/**
 * A skeletal implementation of {@link DDDPredefinedDistribution}, which makes
 * implementing this interface easy for distributions that only use a single
 * type. Classes that extend this need only provide implementations for
 * {@link #getType()} which returns the DDDDamageType this dist uses and
 * {@link #useType(DamageSource, EntityLivingBase)} Which returns {@code true}
 * if and only if this distribution should use its type based off the given
 * DamageSource and EntityLivingBase defender.
 * <p>
 * The constructor takes a {@link Supplier} of booleans, which should always
 * return the config value that enables or disables this distribution. A
 * Supplier is used as this abstracts away checking the config, and also allows
 * the distribution to be enabled/disabled during gameplay.
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractSingleTypeDist extends DDDAbstractPredefinedDistribution {
	private final Supplier<Boolean> config;

	protected AbstractSingleTypeDist(String name, Source src, Supplier<Boolean> config) {
		super(name, src);
		this.config = config;
	}

	@Override
	public boolean enabled() {
		return this.config.get();
	}

	/**
	 * Get the DDDDamageType this distribution uses.
	 * 
	 * @return the DDDDamageType this distribution uses.
	 */
	protected abstract DDDDamageType getType();

	/**
	 * Should this distribution be used in this context?
	 * 
	 * @param source The DamageSource {@code target} was hit with
	 * @param target The defending EntityLivingBase
	 * @return {@code true} if the distribution should be used, {@code false}
	 *         otherwise.
	 */
	protected abstract boolean useType(DamageSource source, EntityLivingBase target);

	@Override
	public final Set<DDDDamageType> getTypes(DamageSource source, EntityLivingBase target) {
		if(this.enabled() && this.useType(source, target)) {
			return ImmutableSet.of(this.getType());
		}
		return ImmutableSet.of();
	}

	@Override
	public final Optional<IDamageDistribution> getDamageDistribution(DamageSource source, EntityLivingBase target) {
		return this.getTypes(source, target).isEmpty() ? Optional.empty() : Optional.of(this.getType().getBaseDistribution());
	}
}
