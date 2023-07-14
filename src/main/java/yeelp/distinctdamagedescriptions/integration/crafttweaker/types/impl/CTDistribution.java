package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import java.util.List;
import java.util.stream.Collectors;

import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDistribution;

public abstract class CTDistribution implements ICTDistribution {

	private final IDistribution dist;

	CTDistribution(IDistribution dist) {
		this.dist = dist;
	}

	@Override
	public List<ICTDDDDamageType> getCategories() {
		return this.dist.getCategories().stream().map(CTDDDDamageType::getFromDamageType).collect(Collectors.toList());
	}

	protected final IDistribution getDist() {
		return this.dist;
	}
}
