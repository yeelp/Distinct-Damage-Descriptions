package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import yeelp.distinctdamagedescriptions.api.impl.CreatureType;
import yeelp.distinctdamagedescriptions.registries.IDDDCreatureTypeRegistry;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDCreatureTypes extends DDDSourcedRegistry<CreatureType> implements IDDDCreatureTypeRegistry {
	private final Map<String, Set<CreatureType>> typeMap = new NonNullMap<String, Set<CreatureType>>(() -> Sets.newHashSet(CreatureType.UNKNOWN));

	public DDDCreatureTypes() {
		super(d -> d.getTypeName(), "Creature Type");
	}

	@Override
	public void init() {
		this.register(CreatureType.UNKNOWN);
	}

	@Override
	public Set<CreatureType> getCreatureTypeForMob(String key) {
		return this.typeMap.get(key);
	}

	@Override
	public void addTypeToEntity(String entityID, CreatureType type) {
		if(this.typeMap.containsKey(entityID)) {
			this.typeMap.get(entityID).add(type);
		}
		else {
			this.typeMap.put(entityID, Sets.newHashSet(type));
		}
	}

	@Override
	public void removeTypeFromEntity(String entityID, CreatureType type) {
		// This works because the NonNullMap will always give something. If entityId
		// doesn't exist, it will give a singleton set containing
		// CreatureTypeData.UNKNOWN. If we happen to remove UNKNOWN then the set will be
		// empty and we remove it from the map. But it was never in the map, so nothing
		// happens and a future get will still give back the same singleton set
		Set<CreatureType> types = this.typeMap.get(entityID);
		if(types.remove(type) && types.isEmpty()) {
			this.typeMap.remove(entityID);
		}
	}

}
