package yeelp.distinctdamagedescriptions.config;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class DDDMetadataAcceptingConfiguration<T> implements IDDDConfiguration<T> {

	private final IDDDConfiguration<T> config;
	
	private DDDMetadataAcceptingConfiguration(IDDDConfiguration<T> config) {
		this.config = config;
	}
	
	public static final <U> IDDDConfiguration<U> createMetadataConfiguration(IDDDConfiguration<U> config) {
		return new DDDMetadataAcceptingConfiguration<U>(config);
	}
	
	@Override
	public Iterator<ConfigEntry<T>> iterator() {
		return this.config.iterator();
	}

	@Override
	public T get(String key) {
		return wrapOperationInMetadataCheck(key, this.config::get, null);
	}

	@Override
	public T getDefaultValue() {
		return this.config.getDefaultValue();
	}

	@Override
	public boolean put(String key, T val) {
		return this.config.put(key, val);
	}

	@Override
	public boolean configured(String key) {
		return wrapOperationInMetadataCheck(key, this.config::configured, false);
	}
	
	static <U> U wrapOperationInMetadataCheck(String key, Function<String, U> operation, U valueToSignalMetadataCheck) {
		U result = operation.apply(key);
		if(Objects.equals(result, valueToSignalMetadataCheck) && key.matches(YResources.ITEM_ID_WITH_META)) {
			return operation.apply(YResources.stripMetadataInfo(key));
		}
		return result;
	}

}
