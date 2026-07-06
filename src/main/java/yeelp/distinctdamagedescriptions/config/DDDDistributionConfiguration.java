package yeelp.distinctdamagedescriptions.config;

import yeelp.distinctdamagedescriptions.capability.IDistribution;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class DDDDistributionConfiguration<T extends IDistribution> extends DDDBaseConfiguration<T> {

	public DDDDistributionConfiguration(Supplier<T> defaultVal) {
		super(defaultVal);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(@Nonnull String key) {
		if(this.configured(key)) {
            //noinspection DataFlowIssue
            return (T) super.get(key).copy();
		}
		return null;
	}
}
