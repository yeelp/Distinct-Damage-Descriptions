package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDBuiltInFire;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDBuiltInForce;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDBuiltInPiercing;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDBuiltInPoison;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDDaylightDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDInstantEffectsDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.EnderPearlDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.ParrotPoisonDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.SimpleBuiltInDist;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.IDDDDistributionRegistry;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public final class DDDDistributions extends DDDSourcedRegistry<DDDPredefinedDistribution> implements IDDDDistributionRegistry {
	public DDDDistributions() {
		super((dist) -> dist.getName(), "Distribution");
	}

	@Override
	public void init() {
		//@formatter:off
		this.registerAll(SimpleBuiltInDist.ANVIL, 
				SimpleBuiltInDist.CACTUS, 
				SimpleBuiltInDist.FALL, 
				SimpleBuiltInDist.FALLING_BLOCK, 
				SimpleBuiltInDist.FLY_INTO_WALL, 
				SimpleBuiltInDist.LIGHTNING, 
				SimpleBuiltInDist.WITHER, 
				SimpleBuiltInDist.SUFFOCATION, 
				DDDBuiltInForce.EVOKER_FANGS_DIST, 
				DDDBuiltInForce.GUARDIAN_DIST, 
				new DDDBuiltInForce.ThornsDist(), 
				new DDDDaylightDist(), 
				new DDDBuiltInFire(), 
				new DDDBuiltInPiercing(), 
				new DDDBuiltInPoison(), 
				new DDDInstantEffectsDist(), 
				new ParrotPoisonDist(), 
				new DDDExplosionDist(), 
				new EnderPearlDist());
		//@formatter:on
	}

	@Override
	public Set<DDDDamageType> getDamageTypes(DamageSource src, EntityLivingBase target) {
		Set<DDDDamageType> types = Collections.emptySet();
		return checkDists(types, (set) -> set.isEmpty(), (dist) -> dist.getTypes(src, target));
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		return checkDists(Optional.empty(), (dist) -> !dist.isPresent(), (dist) -> dist.getDamageDistribution(src, target));
	}

	private <T> T checkDists(T start, Predicate<T> p, Function<DDDPredefinedDistribution, T> next) {
		T result;
		DDDPredefinedDistribution dist = null;
		Iterator<DDDPredefinedDistribution> it = this.map.values().stream().sorted().iterator();
		for(result = start; p.test(result) && it.hasNext(); result = next.apply(dist = it.next()));
		if(ModConfig.showDotsOn) {
			if(dist != null) {
				DebugLib.outputFormattedDebug("Selected Predefined Distribution is: %s", dist.getName());
			}
			else {
				DistinctDamageDescriptions.debug("No Predefined Distribution Applicable for this context.");
			}
		}
		return result;
	}
}
