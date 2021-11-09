package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public interface IDDDDistributionRegistry extends IDDDRegistry<DDDPredefinedDistribution> {
	Set<DDDDamageType> getDamageTypes(DamageSource src, EntityLivingBase target);

	Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target);
}