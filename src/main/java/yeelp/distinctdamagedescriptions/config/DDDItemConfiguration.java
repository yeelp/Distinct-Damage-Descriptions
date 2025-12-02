package yeelp.distinctdamagedescriptions.config;

import java.util.function.Supplier;

import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class DDDItemConfiguration<T> extends DDDBaseConfiguration<T> {

	public DDDItemConfiguration(Supplier<T> defaultVal) {
		super(defaultVal);
	}
	
	@Override
	public boolean configured(String key) {
		boolean configured = super.configured(key);
		if(!configured && key.matches(YResources.ITEM_ID_WITH_META)) {
			return super.configured(YResources.stripMetadataInfo(key));
		}
		return configured;
	}
	
	@Override
	public T get(String key) {
		T candidate = super.get(key);
		if(candidate == null && key.matches(YResources.ITEM_ID_WITH_META)) {
			return super.get(YResources.stripMetadataInfo(key));
		}
		return candidate;
	}
}
