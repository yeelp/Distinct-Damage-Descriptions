package yeelp.distinctdamagedescriptions.registries;

import java.util.Map;
import java.util.Optional;

public interface IDDDMobDamageRegistry extends IDDDRegistry
{
	/**
	 * Get the damage distribution for this mob
	 * @param key the id of the mob
	 * @return {@link Optional#EMPTY} if config generation is turned on and no distribution exists, 
	 * otherwise, an {@link Optional} containing the distribution map, or the default one if none was present.
	 */
	Optional<Map<String, Float>> getMobDamage(String key);
}
