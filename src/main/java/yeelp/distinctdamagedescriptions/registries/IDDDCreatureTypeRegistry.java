package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public interface IDDDCreatureTypeRegistry extends IDDDRegistry<CreatureTypeData> {
	/**
	 * Add a type to an entity with this ID
	 * 
	 * @param entityID
	 * @param type
	 */
	void addTypeToEntity(String entityID, CreatureTypeData type);

	/**
	 * Get the CreatureTypeData(s) for a mob
	 * 
	 * @param entity the EntityLivingBase
	 * @return a Tuple of CreatureTypeData. If the first is
	 *         CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	default Set<CreatureTypeData> getCreatureTypeForMob(EntityLivingBase entity) {
		Optional<String> oLoc = YResources.getEntityIDString(entity);
		if(oLoc.isPresent()) {
			return getCreatureTypeForMob(oLoc.get());
		}
		else {
			return Sets.newHashSet(CreatureTypeData.UNKNOWN);
		}

	}

	/**
	 * Get the CreatureTypeData(s) for a mob
	 * 
	 * @param namespaced id of the mob
	 * @return a Tuple of CreatureTypeData. If the first is
	 *         CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	Set<CreatureTypeData> getCreatureTypeForMob(String key);
}
