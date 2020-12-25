package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;

import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public interface IDDDMobResistancesRegistry extends IDDDRegistry
{
	Optional<MobResistanceCategories> getResistancesForMob(String key);
}
