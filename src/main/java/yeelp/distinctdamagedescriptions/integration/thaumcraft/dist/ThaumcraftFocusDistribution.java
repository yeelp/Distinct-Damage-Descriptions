package yeelp.distinctdamagedescriptions.integration.thaumcraft.dist;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Functions;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thaumcraft.api.aspects.Aspect;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDAbstractPredefinedDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.ThaumcraftConfigurations;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.ThaumcraftFocusTracker;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class ThaumcraftFocusDistribution extends DDDAbstractPredefinedDistribution {

	public ThaumcraftFocusDistribution() {
		super("Thaumcraft Focus", Source.BUILTIN);
	}

	private static final Function<Aspect,IDamageDistribution> ASPECT_MAPPER = Functions.compose(ThaumcraftConfigurations.aspectDists::get, Aspect::getTag);
	
	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		if(isDamageCausedByFocusEffect(src, target)) {
			//@formatter:off
			return ThaumcraftFocusTracker.getInstance().getAspects(target).stream()
				.map(ASPECT_MAPPER)
				.reduce(Sets.newHashSet(), (s, d) -> {
					s.addAll(d.getBaseCategories());
					return s;
				}, (s1, s2) -> {
					s1.addAll(s2);
					return s1;
				});
			//@formatter:on
		}
		return Collections.emptySet();
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		if(isDamageCausedByFocusEffect(src, target)) {
			DistinctDamageDescriptions.debug("Damage caused by focus effects.");
			Collection<Aspect> aspects = ThaumcraftFocusTracker.getInstance().getAspects(target);
			return getCombinedDamageDistributionForAspects(aspects);
		}
		return Optional.empty();
	}
	
	private static boolean isDamageCausedByFocusEffect(DamageSource src, EntityLivingBase target) {
		//Thaumcraft does damage weirdly. The direct source is actually the target taking damage, and the indirect source is the caster.
		if(target.equals(src.getImmediateSource()) && (src.getTrueSource() instanceof EntityLivingBase || src.getTrueSource() == null)) {
			DistinctDamageDescriptions.debug("Valid conditions, checking for Aspects...");
			return ThaumcraftFocusTracker.getInstance().hasTrackedAspectsThisTick(target);
		}
		return false;
	}
	
	public static Optional<IDamageDistribution> getCombinedDamageDistributionForAspects(Collection<Aspect> aspects) {
		if(aspects.isEmpty()) {
			DistinctDamageDescriptions.debug("No Aspects?");
			return Optional.empty();
		}
		//@formatter:off
		DDDBaseMap<Float> map = aspects.stream().map(ASPECT_MAPPER)
			.collect(() -> new DDDBaseMap<Float>(() -> 0.0f), 
					(m, d) -> d.getBaseCategories().stream().forEach((t) -> m.merge(t, d.getBaseWeight(t), Float::sum)), 
					(m1, m2) -> m2.keySet().forEach((t) -> m1.merge(t, m2.get(t), Float::sum)));
		//@formatter:on
		int aspectCount = aspects.size();
		map.replaceAll((type, weight) -> weight / aspectCount);
		return Optional.of(new DamageDistribution(map));
	}

	@Override
	public int priority() {
		return 2;
	}
}
