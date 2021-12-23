package yeelp.distinctdamagedescriptions.util;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.util.math.MathHelper;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DistributionBias {

	private final NonNullMap<DDDDamageType, Float> preferred;
	private final float bias;

	public <M extends NonNullMap<DDDDamageType, Float>> DistributionBias(M preferred, float bias) {
		this.preferred = Objects.requireNonNull(preferred, "Material Bias must have preferred distribution!");
		this.bias = bias;
	}

	public <M extends NonNullMap<DDDDamageType, Float>> Optional<DDDBaseMap<Float>> getBiasedDistributionMap(M base, float biasResistance) {
		float newBias = MathHelper.clamp(this.bias - biasResistance, 0, 1);
		if(newBias == 0) {
			return Optional.empty();
		}
		else if(newBias == 1) {
			return Optional.of(this.getPreferredMapCopy());
		}
		return Optional.of(Stream.concat(this.preferred.keySet().stream(), base.keySet().stream()).distinct().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, (d) -> this.preferred.get(d) * newBias + base.get(d) * (1 - newBias))));
	}

	/**
	 * @return the bias
	 */
	public float getBias() {
		return this.bias;
	}
	
	public DDDBaseMap<Float> getPreferredMapCopy() {
		return this.preferred.keySet().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, this.preferred::get));
	}
}
