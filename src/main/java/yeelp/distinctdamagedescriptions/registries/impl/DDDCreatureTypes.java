package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import yeelp.distinctdamagedescriptions.registries.IDDDCreatureTypeRegistry;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDCreatureTypes extends DDDBaseRegistry<CreatureTypeData> implements IDDDCreatureTypeRegistry {
	private final Map<String, Set<CreatureTypeData>> typeMap = new NonNullMap<String, Set<CreatureTypeData>>(Sets.newHashSet(CreatureTypeData.UNKNOWN));

	public DDDCreatureTypes() {
		super(d -> d.getTypeName(), "Creature Type");
	}

	@Override
	public void init() {
		this.register(CreatureTypeData.UNKNOWN);
	}

	@Override
	public Set<CreatureTypeData> getCreatureTypeForMob(String key) {
		return typeMap.get(key);
	}

	@Override
	public void addTypeToEntity(String entityID, CreatureTypeData type) {
		if(!this.typeMap.containsKey(entityID)) {
			this.typeMap.get(entityID).add(type);
		}
		else {
			this.typeMap.put(entityID, Sets.newHashSet(type));
		}
	}

}
