package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;

import yeelp.distinctdamagedescriptions.util.ComparableTriple;

public interface IDDDMobDamageRegistry extends IDDDRegistry
{
	Optional<ComparableTriple<Float, Float, Float>> getMobDamage(String key);
}
