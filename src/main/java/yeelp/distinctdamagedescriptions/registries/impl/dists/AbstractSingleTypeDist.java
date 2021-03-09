package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDSingleTypeDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

public abstract class AbstractSingleTypeDist implements DDDSingleTypeDistribution
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
	public final DDDDamageType classify(DamageSource source, EntityLivingBase target)
	{
		if(this.enabled() && useType(source, target))
		{
			return this.getType();
		}
		else
		{
			return DDDBuiltInDamageType.NORMAL;
		}
	}
}
