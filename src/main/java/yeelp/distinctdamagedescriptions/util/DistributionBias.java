package yeelp.distinctdamagedescriptions.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.math.MathHelper;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;

public final class DistributionBias {

	private final Map<DDDDamageType, Float> preferred;
	private final float bias;

	public DistributionBias(Map<DDDDamageType, Float> preferred, float bias) {
		this.preferred = Objects.requireNonNull(preferred, "Material Bias must have preferred distribution!");
		this.bias = bias;
	}

	public Optional<Map<DDDDamageType, Float>> getBiasedDistributionMap(Map<DDDDamageType, Float> base, float biasResistance) {
		float newBias = MathHelper.clamp(this.bias - biasResistance, 0, 1);
		if(newBias == 0) {
			return Optional.empty();
		}
		else if(newBias == 1) {
			return Optional.of(this.preferred.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
		}
		return Optional.of(Stream.concat(this.preferred.keySet().stream(), base.keySet().stream()).distinct().collect(DDDBaseMap.typesToDDDBaseMap(0.0f, (d) -> this.preferred.get(d) * newBias + base.get(d) * (1 - newBias))));
	}

	/**
	 * @return the bias
	 */
	public float getBias() {
		return this.bias;
	}
	
	public Map<DDDDamageType, Float> getPreferredMapCopy() {
		return this.preferred.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
}
