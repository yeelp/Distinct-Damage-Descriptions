package yeelp.distinctdamagedescriptions.config;

import java.util.Optional;

/**
 * Stores values from config on startup for easy access.
 * 
 * @author Yeelp
 *
 * @param <T> The type of capability this configuration stores
 */
public interface IDDDConfiguration<T> {
	/**
	 * Get a configuration entry
	 * 
	 * @param key
	 * @return the capability
	 */
	T get(String key);

	/**
	 * Get the default configuration
	 * 
	 * @return the default configuration
	 */
	T getDefaultValue();

	/**
	 * Get a configuration entry
	 * 
	 * @param key
	 * @param val
	 * @return true if the entry was overwritten, false if just added.
	 */
	boolean put(String key, T val);

	/**
	 * Is a key configured?
	 * 
	 * @param key
	 * @return true if configured, false if not
	 */
	boolean configured(String key);

	/**
	 * A fall back approach to {@link #get(String)}, where
	 * {@link #getDefaultValue()} is returned if and only if
	 * {@link #configured(String)} is false. Otherwise, returns the configured entry
	 * like {@link #get(String)}.
	 * 
	 * @param key key
	 * @return The configured entry if it exists, or the default value otherwise.
	 * 
	 * @see #getSafe(String)
	 */
	default T getOrFallbackToDefault(String key) {
		if(this.configured(key)) {
			return this.get(key);
		}
		return this.getDefaultValue();
	}

	/**
	 * A safe version of {@link #get(String)}, that returns {@link Optional#empty()}
	 * if the key isn't configured. A different alternative to
	 * {@link #getOrFallbackToDefault(String)}, if the default value isn't needed.
	 * 
	 * @param key key
	 * @return An Optional containing the result, or an empty Optional if the key
	 *         isn't configured.
	 * 
	 * @see #getOrFallbackToDefault(String)
	 */
	default Optional<T> getSafe(String key) {
		return Optional.ofNullable(this.get(key));
	}
}
