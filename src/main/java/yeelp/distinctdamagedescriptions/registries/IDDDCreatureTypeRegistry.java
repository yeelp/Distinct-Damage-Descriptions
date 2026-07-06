package yeelp.distinctdamagedescriptions.registries;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.impl.CreatureType;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

import java.util.Optional;
import java.util.Set;

public interface IDDDCreatureTypeRegistry extends IDDDRegistry<CreatureType> {
	/**
	 * Add a type to an entity with this ID
	 * 
	 * @param entityID entity's ID
	 * @param type creature type to add
	 */
	void addTypeToEntity(String entityID, CreatureType type);
	
	/**
	 * Remove a type from an entity with this ID
	 * 
	 * @param entityID entity's ID
	 * @param type creature type to remove
	 */
	void removeTypeFromEntity(String entityID, CreatureType type);

	/**
	 * Get the CreatureType(s) for a mob
	 * 
	 * @param entity the EntityLivingBase
	 * @return a Set of CreatureTypes. If the returned Set only contains
	 *         {@link CreatureType#UNKNOWN}, then this mob has no creature type.
	 */
	default Set<CreatureType> getCreatureTypeForMob(EntityLivingBase entity) {
		Optional<String> oLoc = YResources.getEntityIDString(entity);
		if(oLoc.isPresent()) {
			return this.getCreatureTypeForMob(oLoc.get());
		}
		return Sets.newHashSet(CreatureType.UNKNOWN);

	}

	/**
	 * Get the CreatureTypeData(s) for a mob
	 * 
	 * @param key namespaced id of the mob
	 * @return a Set of CreatureType. If the returned Set only contains
	 *         {@link CreatureType#UNKNOWN}, then this mob has no creature type.
	 */
	Set<CreatureType> getCreatureTypeForMob(String key);
}
