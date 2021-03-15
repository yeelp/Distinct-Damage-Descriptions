package yeelp.distinctdamagedescriptions.util.lib;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A Simple map that doesn't allow null Values.
 * @author Yeelp
 *
 * @param <Key> the type of keys of the map
 * @param <Value> the type of values stored in the map
 */
public class NonNullMap<Key, Value> extends HashMap<Key, Value> implements Map<Key, Value>
{
	private Value defaultVal;
	
	private NonNullMap()
	{
		throw new UnsupportedOperationException("A default value must be specifed for the NonNullMap");
	}
	
	/**
	 * Create a new NonNullMap
	 * @param defaultVal the default value stored in the map. The NonNullMap will fall back to this value whenever it encounters a null Value.
	 */
	public NonNullMap(Value defaultVal)
	{
		super();
		this.defaultVal = defaultVal;
	}
	
	/**
	 * Create a new NonNullMap initialized with the specified keys and default value
	 * @param keys the keys to use when initializing the map
	 * @param defaultVal the default value to initialize the map with. The NonNullMap will use this whenever it encounters null.
	 */
	@SafeVarargs
	public NonNullMap(Value defaultVal, Key...keys)
	{
		this(defaultVal);
		for(Key k : keys)
		{
			super.put(k, defaultVal);
		}
	}
	
	/**
	 * Get the default value the map uses. Note updating the default value here will change it across all mappings that are mapped to the default value.
	 * @return the default value
	 */
	public Value getDefaultValue()
	{
		return this.defaultVal;
	}
	
	@Override
	public boolean containsValue(Object value) 
	{
		return value.equals(defaultVal) || super.containsValue(value);
	}
	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * <p> For the NonNullMap, if the key has no mapping, the default value is returned instead.
	 */
	@Override
	public Value get(Object key) 
	{
		return super.getOrDefault(key, defaultVal);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p> Since the NonNullMap disallows null keys and values, this method throws an {@link UnsupportedOperationException} if either the key or value is null.
	 * @throws UnsupportedOperationException if the key or value is null
	 */
	@Override
	public Value put(Key key, Value value) 
	{
		if(value == null)
		{
			throw new UnsupportedOperationException("Null values disallowed for NonNullMap");
		}
		else if(key == null)
		{
			throw new UnsupportedOperationException("Null keys disallowed for NonNullMap");
		}
		return super.put(key, value);
	}
	/**
	 * Maps the specified key to the default value, and returns whatever the original value associated with this key was.
	 * @param key the key that should have its mapping set to the default value.
	 * @return the value once associated with this key This method returns null if, and only if, this map never contained the specified key.
	 */
	@Nullable
	public Value setDefault(Key key)
	{
		return super.put(key, defaultVal);
	}
}
