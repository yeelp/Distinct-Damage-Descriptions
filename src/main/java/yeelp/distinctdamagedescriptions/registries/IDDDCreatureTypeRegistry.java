package yeelp.distinctdamagedescriptions.registries;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.impl.CreatureType;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public interface IDDDCreatureTypeRegistry extends IDDDRegistry<CreatureType> {
	/**
	 * Add a type to an entity with this ID
	 * 
	 * @param entityID
	 * @param type
	 */
	void addTypeToEntity(String entityID, CreatureType type);
	
	/**
	 * Remove a type from an entity with this ID
	 * 
	 * @param entityID
	 * @param type
	 */
	void removeTypeFromEntity(String entityID, CreatureType type);

	/**
	 * Get the CreatureTypeData(s) for a mob
	 * 
	 * @param entity the EntityLivingBase
	 * @return a Set of CreatureTypeData. If the first is
	 *         CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	default Set<CreatureType> getCreatureTypeForMob(EntityLivingBase entity) {
		Optional<String> oLoc = YResources.getEntityIDString(entity);
		if(oLoc.isPresent()) {
			return getCreatureTypeForMob(oLoc.get());
		}
		return Sets.newHashSet(CreatureType.UNKNOWN);

	}

	/**
	 * Get the CreatureTypeData(s) for a mob
	 * 
	 * @param namespaced id of the mob
	 * @return a Tuple of CreatureTypeData. If the first is
	 *         CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	Set<CreatureType> getCreatureTypeForMob(String key);
}
