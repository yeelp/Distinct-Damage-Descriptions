package yeelp.distinctdamagedescriptions.registries;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

import java.util.Optional;
import java.util.Set;

public interface IDDDDistributionRegistry extends IDDDRegistry<DDDPredefinedDistribution> {
	@SuppressWarnings("unused")
	Set<DDDDamageType> getDamageTypes(DamageSource src, EntityLivingBase target);

	Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target);
}