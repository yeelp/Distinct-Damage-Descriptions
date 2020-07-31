package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDMobDamageRegistry extends IDDDRegistry
{
	ComparableTriple<Float, Float, Float> getMobDamage(String key);
}
