package yeelp.distinctdamagedescriptions.api;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public interface DDDMultiTypeDistribution extends DDDPredefinedDistribution
{
	Set<DDDDamageType> classify(DamageSource source, EntityLivingBase target);
	
	IDamageDistribution getDamageDistribution();
}
