package yeelp.distinctdamagedescriptions.registries;

import java.util.Map;
import java.util.Optional;

import net.minecraft.entity.Entity;

public interface IDDDProjectilePropertiesRegistry extends IDDDRegistry
{
	/**
	 * Get the damage distribution for a projectile.
	 * @param key the id of the projectile, not the item.
	 * @return {@link Optional#EMPTY} if config generation is turned on and no distribution exists, 
	 * otherwise, an {@link Optional} containing the distribution map, or the default one if none was present.
	 */
    Optional<Map<String, Float>> getProjectileDamageTypes(String key);
    
    /**
     * Get the damage distribution for a projectile based off it's item form.
     * @param itemID the id of the item
     * @return a Map of the distribution if the item was launched as a projectile.
     */
    Map<String, Float> getProjectileDamageTypesFromItemID(String itemID);
    
    /**
     * Check if a projectile Entity is registered
     * @param projectile the Entity to check
     * @return true if registered in the DDD registry, false if not.
     */
    boolean isProjectileRegistered(Entity projectile);
}
