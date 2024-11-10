package yeelp.distinctdamagedescriptions.integration.capability;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public abstract class AbstractBiasedDamageDistribution extends ModUpdatingDamageDistribution {

	protected AbstractBiasedDamageDistribution() {
		super();
	}

	protected AbstractBiasedDamageDistribution(IDamageDistribution base) {
		this(base.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, base::getWeight)));
	}

	protected AbstractBiasedDamageDistribution(Map<DDDDamageType, Float> weights) {
		super(weights);
	}

	protected abstract Set<DDDBaseMap<Float>> computeBiasedMaps(ItemStack owner);
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(ItemStack owner) {
		Set<DDDBaseMap<Float>> maps = this.computeBiasedMaps(owner);
		int size = maps.size();
		if(size != 0) {
			DDDBaseMap<Float> newWeights = new DDDBaseMap<Float>(() -> 0.0f);
			maps.forEach((m) -> m.forEach((k, v) -> newWeights.merge(k, v / size, Float::sum)));
			return Optional.of(newWeights);
		}
		return Optional.empty();
	}

}
