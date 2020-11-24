package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDProjectilePropertiesRegistry extends IDDDRegistry
{
    Optional<ComparableTriple<Float, Float, Float>> getProjectileDamageTypes(String key);
    
    ComparableTriple<Float, Float, Float> getProjectileDamageTypesFromItemID(String itemID);
    
    boolean isProjectileRegistered(Entity projectile);
}
