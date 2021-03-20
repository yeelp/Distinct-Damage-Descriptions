package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.registries.IDDDRegistry;

public abstract class DDDBaseRegistry<T> implements IDDDRegistry<T>
{
	protected final Map<String, T> map = new HashMap<String, T>();
	private Function<T, String> keyFunc;
	private final String name;
	DDDBaseRegistry(Function<T, String> f, String name)
	{
		this.keyFunc = f;
		this.name = name;
		this.init();
	}
	
	@Override
	public void register(boolean suppressOutput, T obj)
	{
		if(this.isRegistered(obj))
		{
			throw new RuntimeException(obj.toString() + "was already registered!");
		}
		else
		{
			String key = keyFunc.apply(obj);
			this.map.put(key, obj);
			if(!suppressOutput)
			{
				DistinctDamageDescriptions.info(String.format("Registering %s: %s", name, key));
			}
		}
	}
	
	@Override
	public void registerAll(boolean suppressOutput, @SuppressWarnings("unchecked") T...objs)
	{
		for(T t : objs)
		{
			this.register(suppressOutput, t);
		}
	}
	
	@Override
	public T get(String key)
	{
		return map.get(key);
	}
	
	@Override
	public boolean isRegistered(T obj)
	{
		return this.map.containsKey(keyFunc.apply(obj));
	}
}
