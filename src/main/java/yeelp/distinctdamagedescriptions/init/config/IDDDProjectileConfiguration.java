package yeelp.distinctdamagedescriptions.init.config;

import java.util.Map;
import java.util.Optional;

import net.minecraft.entity.Entity;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.IDDDRegistry;

public interface IDDDProjectileConfiguration extends IDDDConfiguration<IDamageDistribution>
{   
    /**
     * Get the damage distribution for a projectile based off it's item form.
     * @param itemID the id of the item
     * @return a Map of the distribution if the item was launched as a projectile.
     */
    IDamageDistribution getFromItemID(String itemID);
    
    /**
     * Link a projectile and it's item together for tooltips. 
     * @param itemID item id
     * @param projID projectile id
     */
    void registerItemProjectilePair(String itemID, String projID);
    
    /**
     * Check if a projectile pair is registered by the Entity
     * @param projectile the Entity to check
     * @return true if registered
     */
    boolean isProjectilePairRegistered(Entity projectile);
    
    /**
     * Check if a projectile pair is registered by its item id.
     * @param itemID
     * @return true if registered
     */
    boolean isProjectilePairRegistered(String itemID);
}
