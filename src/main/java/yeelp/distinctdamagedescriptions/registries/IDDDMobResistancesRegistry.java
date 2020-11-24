package yeelp.distinctdamagedescriptions.registries;

import java.util.Map;
import java.util.Optional;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

public interface IDDDMobResistancesRegistry extends IDDDRegistry
{
	public Optional<MobResistanceCategories> getResistancesForMob(String key);
}
