package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Functions;

import electroblob.wizardry.entity.living.EntityMagicSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public final class WizardrySlimeDistribution extends DDDAbstractPredefinedDistribution {
	
	private static final Function<Entity, Optional<IDamageDistribution>> DISTRIBUTION_EXTRACTOR = Functions.compose(DDDAPI.accessor::getDamageDistribution, EntityLivingBase.class::cast);
	
	public WizardrySlimeDistribution() {
		super("wizardry slime", Source.BUILTIN);
	}

	@Override
	public boolean enabled() {
		return true;
	}
	
	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		if(src == DamageSource.MAGIC) {
			getSlimeDamageDistribution(target).map(IDamageDistribution::getCategories).orElse(Collections.emptySet());
		}
		return Collections.emptySet();
	}
	
	
	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		if(src == DamageSource.MAGIC) {
			return getSlimeDamageDistribution(target); 
		}
		return Optional.empty();
	}
	
	private static Optional<IDamageDistribution> getSlimeDamageDistribution(EntityLivingBase target) {
		return target.getPassengers().stream().filter((entity) -> entity instanceof EntityMagicSlime && entity.ticksExisted % 16 == 1).findFirst().flatMap(DISTRIBUTION_EXTRACTOR);
	}
}
