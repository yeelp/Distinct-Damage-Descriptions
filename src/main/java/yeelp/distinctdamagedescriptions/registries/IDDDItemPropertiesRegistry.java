package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;

import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDItemPropertiesRegistry extends IDDDRegistry
{
	Optional<ComparableTriple<Float, Float, Float>> getDamageDistributionForItem(String key);
	
	ComparableTriple<Float, Float, Float> getDefaultDamageDistribution();
	
	boolean doesItemHaveCustomDamageDistribution(String key);
	
	Optional<ComparableTriple<Float, Float, Float>> getArmorDistributionForItem(String key);
	
	ComparableTriple<Float, Float, Float> getDefaultArmorDistribution();
	
	boolean doesArmorHaveCustomArmorDistribution(String key);
}
