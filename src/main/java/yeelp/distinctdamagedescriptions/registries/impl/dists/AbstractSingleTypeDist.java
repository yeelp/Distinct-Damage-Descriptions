package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
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
 * the distribution to be enabled/disabled during gameplay. If you want the
 * distribution to always be enabled (i.e. there is no such config values that
 * toggles it on or off), use the no argument constructor.
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractSingleTypeDist implements DDDPredefinedDistribution {
	private final Supplier<Boolean> config;

	AbstractSingleTypeDist() {
		this(() -> true);
	}

	AbstractSingleTypeDist(Supplier<Boolean> config) {
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
		DDDDamageType type;
		if(this.enabled() && useType(source, target)) {
			type = this.getType();
		}
		else {
			type = DDDBuiltInDamageType.NORMAL;
		}
		return Sets.newHashSet(type);
	}

	@Override
	public final IDamageDistribution getDamageDistribution(DamageSource source, EntityLivingBase target) {
		return getTypes(source, target).iterator().next().getBaseDistribution();
	}
}
