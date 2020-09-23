package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDItemPropertiesRegistry extends IDDDRegistry
{
	ComparableTriple<Float, Float, Float> getDamageDistributionForItem(String key);
	
	boolean doesItemHaveCustomDamageDistribution(String key);
	
	ComparableTriple<Float, Float, Float> getArmorDistributionForItem(String key);
	
	boolean doesArmorHaveCustomArmorDistribution(String key);
}
