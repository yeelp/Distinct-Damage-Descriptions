package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.config.ModConfig;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Base registry for DDD
 * 
 * @author Yeelp
 *
 * @param <T> the type registered in the registry
 */
public interface IDDDRegistry<T> extends Iterable<T> {
	/**
	 * Initialize this registry
	 */
	void init();

	/**
	 * Register an object
	 * 
	 * @param obj object to register
	 */
	default void register(T obj) {
		this.register(ModConfig.core.suppressRegistrationInfo, obj);
	}

	/**
	 * Register an object
	 * 
	 * @param suppressOutput true if log output should be suppressed for this
	 *                       registration.
	 * @param obj object to register
	 */
	void register(boolean suppressOutput, T obj);

	/**
	 * Register a collection of objects
	 * 
	 * @param objs objects to register
	 */
	@SuppressWarnings("unchecked")
    default void registerAll(T... objs) {
		this.registerAll(ModConfig.core.suppressRegistrationInfo, objs);
	}

	/**
	 * Register a collection of objects
	 * 
	 * @param suppressOutput true if log output should be suppressed
	 * @param objs objects to register
	 */
	@SuppressWarnings("unchecked")
	void registerAll(boolean suppressOutput, T... objs);

	/**
	 * Check if an object is registered
	 * 
	 * @param obj object to check
	 * @return true if registered
	 */
	boolean isRegistered(T obj);

	/**
	 * Get a value from the registry
	 * 
	 * @param key key to use
	 * @return The value from the registry, null if not present.
	 */
	@Nullable
	T get(String key);

	/**
	 * Get all registered values
	 * 
	 * @return all registered values
	 */
	Collection<T> getAll();
}
