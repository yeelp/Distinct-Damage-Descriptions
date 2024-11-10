package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.api.impl.AbstractDDDCapModifier;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class DDDDistributionModifierRegistry<T extends ICapabilityProvider, D extends IDistribution> extends DDDModifierRegistry<T, D, IDDDCapModifier<T>> {

	public DDDDistributionModifierRegistry(Supplier<Float> defaultVal, String type) {
		super(defaultVal, type);
	}

	@Override
	public void applyModifiers(T provider, D cap) {
		DDDBaseMap<Float> weights = new DDDBaseMap<Float>(this.defaultVal);
		cap.getCategories().forEach((type) -> weights.put(type, cap.getWeight(type)));
		this.poolApplicableModifiers(provider).forEach((modifier) -> {			
			if(modifier.shouldReallocate()) {
				float initialWeightToSubtract = (float) (YMath.sum(modifier.values()) / weights.size());
				float newWeightToSubtract = 0.0f;
				Set<DDDDamageType> typesToRemove = Sets.newHashSet();
				do {
					typesToRemove.clear();
					for(DDDDamageType type : weights.keySet()) {
						float lastWeight = weights.put(type, weights.get(type) - initialWeightToSubtract);
						if(lastWeight <= initialWeightToSubtract) {
							typesToRemove.add(type);
							newWeightToSubtract += initialWeightToSubtract - lastWeight;
						}
					}
					typesToRemove.forEach(weights::remove);
					initialWeightToSubtract = newWeightToSubtract;
				}while(typesToRemove.size() > 0);
			}
			modifier.forEach((type, weight) -> {
				weights.merge(type, weight, Float::sum);
			});
		});
		this.scaleWeights(weights);
		cap.setNewWeights(weights);
	}
	
	@SuppressWarnings("static-method")
	protected void scaleWeights(@SuppressWarnings("unused") DDDBaseMap<Float> map) {
		return;
	}
	
	public static final class DDDDamageDistributionModifierRegistry<T extends ICapabilityProvider> extends DDDDistributionModifierRegistry<T, IDamageDistribution> {
		public DDDDamageDistributionModifierRegistry(String type) {
			super(() -> 0.0f, type);
		}
		
		@Override
		protected void scaleWeights(DDDBaseMap<Float> map) {
			float scaleFactor = (float) (1.0f / YMath.sum(map.values()));
			map.replaceAll((type, weight) -> scaleFactor * weight);
		}
	}
	
	private final class PooledDistributionModifier extends AbstractDDDCapModifier<T> {

		protected PooledDistributionModifier(IDDDCapModifier<T> modifier, boolean shouldReallocate) {
			super(DDDDistributionModifierRegistry.this.defaultVal, null, shouldReallocate);
			this.putAll(modifier);
		}

		@Override
		public AppliesTo getAppliesToEnum() {
			return null;
		}

		@Override
		public boolean applicable(T t) {
			return false;
		}

		@Override
		public String getName() {
			return null;
		}
		
		PooledDistributionModifier combine(IDDDCapModifier<T> other) {
			other.forEach((type, weight) -> this.merge(type, weight, Float::sum));
			return this;
		}
	}

	@Override
	protected IDDDCapModifier<T> getNewModifierAccumulator(IDDDCapModifier<T> modifier) {
		return new PooledDistributionModifier(modifier, modifier.shouldReallocate());
	}

	@Override
	protected IDDDCapModifier<T> combine(IDDDCapModifier<T> m1, IDDDCapModifier<T> m2) {
		if(m1 instanceof DDDDistributionModifierRegistry.PooledDistributionModifier) {
			return ((PooledDistributionModifier) m1).combine(m2);
		}
		return (new PooledDistributionModifier(m1, m1.shouldReallocate())).combine(m2);
	}
}
