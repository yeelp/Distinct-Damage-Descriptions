package yeelp.distinctdamagedescriptions.config;

import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class DDDBaseConfiguration<T> implements IDDDConfiguration<T> {
	//we use regular map over NonNullMap because while we don't want null values in the map, we don't want to return a default value for non-configured keys
	private final Map<String, T> map = Maps.newHashMap();
	protected final Supplier<T> defaultVal;

	public DDDBaseConfiguration(Supplier<T> defaultVal) {
		this.defaultVal = defaultVal;
	}

	@Override
	public T get(@Nonnull String key) {
		return this.map.get(key);
	}

	@Nonnull
	@Override
	public T getDefaultValue() {
		return this.defaultVal.get();
	}

	@Override
	public boolean put(@Nonnull String key, @Nonnull T val) {
		boolean present = this.map.containsKey(key);
		this.map.put(key, val);
		return present;
	}

	@Override
	public boolean configured(String key) {
		return this.map.containsKey(key);
	}
	
	@Nonnull
	@Override
	public Iterator<ConfigEntry<T>> iterator() {
		return this.new ConfigurationIterator();
	}
	
	public IDDDConfiguration<T> wrapInMetadataAcceptingConfiguration() {
		return DDDMetadataAcceptingConfiguration.createMetadataConfiguration(this);
	}
	
	private final class ConfigurationIterator implements Iterator<ConfigEntry<T>> {

		private final Iterator<String> keys;
		@SuppressWarnings("synthetic-access")
		ConfigurationIterator() {
			this.keys = DDDBaseConfiguration.this.map.keySet().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return this.keys.hasNext();
		}

		@Override
		public ConfigEntry<T> next() {
			String key = this.keys.next();
            //noinspection DataFlowIssue
            return new ConfigEntry<>(key, DDDBaseConfiguration.this.get(key));
		}
		
	}
}
