package yeelp.distinctdamagedescriptions.registries;

import javax.annotation.Nullable;

/**
 * Base registry for DDD
 * @author Yeelp
 *
 * @param <T> the type registered in the registry
 */
public interface IDDDRegistry<T>
{
	/**
	 * Initialize this registry
	 */
	void init();
	
	/**
	 * Register an object
	 * @param obj
	 */
	void register(T obj);
	
	/**
	 * Register a collection of objects
	 * @param objs
	 */
	void registerAll(@SuppressWarnings("unchecked") T... objs);
	
	/**
	 * Check if an object is registered
	 * @param obj
	 * @return true if registered
	 */
	boolean isRegistered(T obj);
	
	/**
	 * Get a value from the registry
	 * @param key
	 * @return The value from the registry, null if not present.
	 */
	@Nullable
	T get(String key);
}
