package yeelp.distinctdamagedescriptions.config;

import java.util.HashMap;
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
}
