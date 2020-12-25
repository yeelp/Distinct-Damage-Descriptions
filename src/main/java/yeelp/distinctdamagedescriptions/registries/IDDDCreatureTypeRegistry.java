package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;

public interface IDDDCreatureTypeRegistry extends IDDDRegistry
{
	
	
	/**
	 * Get the CreatureTypeData(s) for a mob
	 * @param entity the EntityLivingBase
	 * @return a Tuple of CreatureTypeData. If the first is CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	default Set<CreatureTypeData> getCreatureTypeForMob(EntityLivingBase entity)
	{
		return getCreatureTypeForMob(EntityList.getKey(entity).toString());
	}
	
	/**
	 * Get the CreatureTypeData(s) for a mob
	 * @param namespaced id of the mob
	 * @return a Tuple of CreatureTypeData. If the first is CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	Set<CreatureTypeData> getCreatureTypeForMob(String key);
	
	/**
	 * Get CreatureTypeData from it's name
	 * @param key the name
	 * @return the CreatureTypeData
	 */
	CreatureTypeData getCreatureTypeData(String key);
}
