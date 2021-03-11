package yeelp.distinctdamagedescriptions.init.config;

import java.util.HashMap;
import java.util.Map;

public class DDDBaseConfiguration<T> implements IDDDConfiguration<T>
{
	private final Map<String, T> map = new HashMap<String, T>();
	private final T defaultVal;
	
	DDDBaseConfiguration(T defaultVal)
	{
		this.defaultVal = defaultVal;
	}
	
	@Override
	public T get(String key)
	{
		return this.map.get(key);
	}
	
	@Override
	public T getDefaultValue()
	{
		return this.defaultVal;
	}
	
	@Override
	public boolean put(String key, T val)
	{
		boolean present = map.containsKey(key);
		map.put(key, val);
		return present;
	}
	
	@Override
	public boolean configured(String key)
	{
		return map.containsKey(key);
	}
}
