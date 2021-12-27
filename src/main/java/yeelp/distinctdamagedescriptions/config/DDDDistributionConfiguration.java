package yeelp.distinctdamagedescriptions.config;

import java.util.function.Supplier;

import yeelp.distinctdamagedescriptions.capability.IDistribution;

public class DDDDistributionConfiguration<T extends IDistribution> extends DDDBaseConfiguration<T> {

	public DDDDistributionConfiguration(Supplier<T> defaultVal) {
		super(defaultVal);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String key) {
		if(this.configured(key)) {
			return (T) super.get(key).copy();
		}
		return null;
	}
}
