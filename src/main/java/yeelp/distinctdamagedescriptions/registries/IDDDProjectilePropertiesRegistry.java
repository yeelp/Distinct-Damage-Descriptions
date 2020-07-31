package yeelp.distinctdamagedescriptions.registries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDProjectilePropertiesRegistry extends IDDDRegistry
{
    public ComparableTriple<Float, Float, Float> getProjectileDamageTypes(String key);
    
    public ComparableTriple<Float, Float, Float> getProjectileDamageTypesFromItemID(String itemID);
}
