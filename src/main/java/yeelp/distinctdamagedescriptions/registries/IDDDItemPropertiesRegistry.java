package yeelp.distinctdamagedescriptions.registries;

import java.util.Map;
import java.util.Optional;

public interface IDDDItemPropertiesRegistry extends IDDDRegistry
{
	/**
	 * Get the damage distribution map for a certain item.
	 * @param key the id of the item.
	 * @return {@link Optional#EMPTY} if config generation is turned on and no distribution exists, 
	 * otherwise, an {@link Optional} containing the distribution map, or the default one if none was present.
	 */
	Optional<Map<String, Float>> getDamageDistributionForItem(String key);
	
	/**
	 * Get the default damage distribution.
	 * @return the default damage distribution - a Map with the key value pair ("bludgeoning", 1.0f) and everything else mapped to 0.0f.
	 */
	Map<String, Float> getDefaultDamageDistribution();
	
	/**
	 * Check if the item has a custom damage distribution set
	 * @param key the id of the item
	 * @return true if it has a distribution set in the config, false if not.
	 */
	boolean doesItemHaveCustomDamageDistribution(String key);
	
	/**
	 * Get the armor distribution map for a certain armor piece.
	 * @param key the id of the armor piece in question.
	 * @return {@link Optional#EMPTY} if config generation is turned on and no distribution exists, 
	 * otherwise, an {@link Optional} containing the distribution map, or the default one if none was present.
	 */
	Optional<Map<String, Float>> getArmorDistributionForItem(String key);
	
	/**
	 * Get the default armor distribution
	 * @return a Map with all values mapped to 0.0f.
	 */
	Map<String, Float> getDefaultArmorDistribution();
	
	/**
	 * Check if the armor has a custom armor distribution set.
	 * @param key the id of the armor.
	 * @return true if it has a distribution set in the config, false if not.
	 */
	boolean doesArmorHaveCustomArmorDistribution(String key);
	
	/**
	 * Get the shield distribution map for a certain shield.
	 * @param key the id of the shield
	 * @return {@link Optional#EMPTY} if config generation is turned on and no distribution exists, 
	 * otherwise, an {@link Optional} containing the distribution map, or the default one if none was present.
	 */
	Optional<Map<String, Float>> getShieldDistribution(String key);
}
