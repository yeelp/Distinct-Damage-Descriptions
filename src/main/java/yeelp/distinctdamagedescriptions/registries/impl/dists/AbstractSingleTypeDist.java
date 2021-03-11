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

public abstract class AbstractSingleTypeDist implements DDDPredefinedDistribution
{
	private final Supplier<Boolean> config;
	
	AbstractSingleTypeDist(Supplier<Boolean> config)
	{
		this.config = config;
	}
	
	@Override
	public boolean enabled()
	{
		return this.config.get();
	}
	
	protected abstract DDDDamageType getType();
	
	protected abstract boolean useType(DamageSource source, EntityLivingBase target);
	
	@Override
	public final Set<DDDDamageType> getTypes(DamageSource source, EntityLivingBase target)
	{
		DDDDamageType type;
		if(this.enabled() && useType(source, target))
		{
			type = this.getType();
		}
		else
		{
			type = DDDBuiltInDamageType.NORMAL;
		}
		return Sets.newHashSet(type);
	}
	
	@Override
	public final IDamageDistribution getDamageDistribution(DamageSource source, EntityLivingBase target)
	{
		return getTypes(source, target).iterator().next().getBaseDistribution();
	}
}
