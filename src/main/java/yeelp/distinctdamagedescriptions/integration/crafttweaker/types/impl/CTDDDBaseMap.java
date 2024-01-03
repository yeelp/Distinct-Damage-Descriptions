package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDBaseMap;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class CTDDDBaseMap extends NonNullMap<ICTDDDDamageType, Float> implements ICTDDDBaseMap {
	
	public CTDDDBaseMap(DDDBaseMap<Float> map) {
		super(() -> map.getDefaultValue());
		map.forEach((type, value) -> super.put(CTDDDDamageType.getFromDamageType(type), value));
	}

	@Override
	public float get(ICTDDDDamageType type) {
		return super.get(type);
	}

	@Override
	public boolean contains(ICTDDDDamageType type) {
		return this.containsKey(type);
	}

}
