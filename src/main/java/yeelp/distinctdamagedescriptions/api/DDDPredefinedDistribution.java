package yeelp.distinctdamagedescriptions.api;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

public interface DDDPredefinedDistribution
{
	boolean enabled();
	
	Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target);
	
	String getName();
	
	IDamageDistribution getDamageDistribution(DamageSource src, EntityLivingBase target);
}
