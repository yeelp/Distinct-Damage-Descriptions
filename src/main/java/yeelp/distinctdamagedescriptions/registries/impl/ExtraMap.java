package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDMultiTypeDistribution;
import yeelp.distinctdamagedescriptions.api.DDDSingleTypeDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInFire;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInForce;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInPiercing;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDBuiltInPoison;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDInstantEffectsDist;
import yeelp.distinctdamagedescriptions.registries.impl.dists.SimpleBuiltInDist;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;

public final class ExtraMap
{
	//TODO ADD burning in daylight dist
	private final List<DDDSingleTypeDistribution> singleDists = Lists.newArrayList(SimpleBuiltInDist.ANVIL, 
																			  SimpleBuiltInDist.CACTUS, 
																			  SimpleBuiltInDist.FALL, 
																			  SimpleBuiltInDist.FALLING_BLOCK, 
																			  SimpleBuiltInDist.FLY_INTO_WALL, 
																			  SimpleBuiltInDist.LIGHTNING, 
																			  SimpleBuiltInDist.WITHER, 
																			  DDDBuiltInForce.EVOKER_FANGS_DIST, 
																			  DDDBuiltInForce.GUARDIAN_DIST, 
																			  new DDDBuiltInForce.ThornsDist(), 
																			  new DDDBuiltInFire(), 
																			  new DDDBuiltInPiercing(),
																			  new DDDBuiltInPoison(), 
																			  new DDDInstantEffectsDist());
	private final List<DDDMultiTypeDistribution> multiDists = Lists.newArrayList(new DDDExplosionDist());
	
	Set<DDDDamageType> get(LivingAttackEvent evt)
	{
		DamageSource src = evt.getSource();
		EntityLivingBase entity = evt.getEntityLiving();
		Set<DDDDamageType> set = checkMultiDists(src, entity);
		if(set.isEmpty())
		{	
			return Sets.newHashSet(checkSingleDists(src, evt.getEntityLiving()));
		}
		else
		{
			return set;
		}
	}
	
	private DDDDamageType checkSingleDists(DamageSource src, EntityLivingBase entity)
	{
		Iterator<DDDSingleTypeDistribution> it = singleDists.iterator();
		DDDDamageType type;
		for(type = DDDBuiltInDamageType.NORMAL; type == DDDBuiltInDamageType.NORMAL && it.hasNext(); type = it.next().classify(src, entity));
		return type;
	}
	
	private Set<DDDDamageType> checkMultiDists(DamageSource src, EntityLivingBase entity)
	{
		Iterator<DDDMultiTypeDistribution> it = multiDists.iterator();
		Set<DDDDamageType> types;
		for(types = Collections.emptySet(); types.isEmpty() && it.hasNext(); it.next().classify(src, entity));
		return types;
	}
}
