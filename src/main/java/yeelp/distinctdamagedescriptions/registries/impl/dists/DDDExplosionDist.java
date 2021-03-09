package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDMultiTypeDistribution;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;

public final class DDDExplosionDist implements DDDMultiTypeDistribution
{
	private static IDamageDistribution dist;
	
	static
	{
		update();
	}
	
	@Override
	public boolean enabled()
	{
		return ModConfig.dmg.extraDamage.enableExplosionDamage;
	}

	@Override
	public Set<DDDDamageType> classify(DamageSource source, EntityLivingBase target)
	{
		return source.isExplosion() && this.enabled() ? dist.getCategories() : Collections.emptySet();
	}

	@Override
	public IDamageDistribution getDamageDistribution()
	{
		return dist;
	}
	
	public static void update()
	{
		if(ModConfig.dmg.extraDamage.enableExplosionDamage)
		{
			dist = new DamageDistribution(DDDConfigReader.buildMap(0.0f, DDDConfigReader.parseListOfTuples(ModConfig.dmg.extraDamage.explosionDist)));
		}
	}

}
