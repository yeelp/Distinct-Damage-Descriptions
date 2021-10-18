package yeelp.distinctdamagedescriptions.config;

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
	 * Get the default distribution
	 * 
	 * @return the default distribution
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
	
	default T getOrFallbackToDefault(String key) {
		if(this.configured(key)) {
			return this.get(key);
		}
		return this.getDefaultValue();
	}
}
