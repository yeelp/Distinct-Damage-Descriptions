package yeelp.distinctdamagedescriptions.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class DDDBaseConfiguration<T> implements IDDDConfiguration<T> {
	private final Map<String, T> map = new HashMap<String, T>();
	protected final Supplier<T> defaultVal;

	public DDDBaseConfiguration(Supplier<T> defaultVal) {
		this.defaultVal = defaultVal;
	}

	@Override
	public T get(String key) {
		return this.map.get(key);
	}

	@Override
	public T getDefaultValue() {
		return this.defaultVal.get();
	}

	@Override
	public boolean put(String key, T val) {
		boolean present = this.map.containsKey(key);
		this.map.put(key, val);
		return present;
	}

	@Override
	public boolean configured(String key) {
		return this.map.containsKey(key);
	}
	
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
			return new ConfigEntry<T>(key, DDDBaseConfiguration.this.get(key));
		}
		
	}
}
