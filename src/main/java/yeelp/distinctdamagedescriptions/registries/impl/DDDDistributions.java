package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.IDDDDistributionRegistry;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInFire;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInForce;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInPiercing;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInPoison;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDDaylightDist;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDInstantEffectsDist;
import yeelp.distinctdamagedescriptions.registries.impl.dists.SimpleBuiltInDist;

public final class DDDDistributions extends DDDBaseRegistry<DDDPredefinedDistribution> implements IDDDDistributionRegistry
{
	public DDDDistributions()
	{
		super((dist) -> dist.getName(), "Distribution");
	}

	@Override
	public void init()
	{
		this.registerAll(SimpleBuiltInDist.ANVIL, 
						 SimpleBuiltInDist.CACTUS, 
						 SimpleBuiltInDist.FALL, 
						 SimpleBuiltInDist.FALLING_BLOCK, 
						 SimpleBuiltInDist.FLY_INTO_WALL, 
						 SimpleBuiltInDist.LIGHTNING, 
						 SimpleBuiltInDist.WITHER, 
						 DDDBuiltInForce.EVOKER_FANGS_DIST, 
						 DDDBuiltInForce.GUARDIAN_DIST, 
						 new DDDBuiltInForce.ThornsDist(),
						 new DDDDaylightDist(),
						 new DDDBuiltInFire(), 
						 new DDDBuiltInPiercing(),
						 new DDDBuiltInPoison(), 
						 new DDDInstantEffectsDist(), 
						 new DDDExplosionDist());
	}

	@Override
	public Set<DDDDamageType> getDamageTypes(DamageSource src, EntityLivingBase target)
	{
		Set<DDDDamageType> types = Collections.emptySet();
		return checkDists(types, (set) -> set.isEmpty(), (dist) -> dist.getTypes(src, target), src, target);
	}

	@Override
	public IDamageDistribution getDamageDistribution(DamageSource src, EntityLivingBase target)
	{
		return checkDists(null, (dist) -> dist == null, (dist) -> dist.getDamageDistribution(src, target), src, target);
	}
	
	private <T> T checkDists(T start, Predicate<T> p, Function<DDDPredefinedDistribution, T> next, DamageSource src, EntityLivingBase entity)
	{
		T result;
		Iterator<DDDPredefinedDistribution> it = map.values().iterator();
		for(result = start; p.test(result) && it.hasNext(); next.apply(it.next()));
		return result;
	}
}
