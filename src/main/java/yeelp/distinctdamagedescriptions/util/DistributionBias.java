package yeelp.distinctdamagedescriptions.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Functions;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;

public final class DistributionBias {

	private final Map<DDDDamageType, Float> preferred;
	private final float bias;

	public DistributionBias(Map<DDDDamageType, Float> preferred, float bias) {
		this.preferred = preferred;
		this.bias = Math.min(bias, 1.0f);
	}

	public <D extends IDistribution> Optional<Map<DDDDamageType, Float>> getBiasedDistributionMap(D dist, float biasResistance) {
		float newBias = Math.max(this.bias - biasResistance, 0);
		if(newBias == 0) {
			return Optional.empty();
		}
		else if(newBias == 1) {
			return Optional.of(this.preferred.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
		}
		return Optional.of(Stream.concat(this.preferred.keySet().stream(), dist.getCategories().stream()).distinct().collect(Collectors.toMap(Functions.identity(), (d) -> this.preferred.get(d) * newBias + dist.getWeight(d) * (1 - newBias))));
	}
}
