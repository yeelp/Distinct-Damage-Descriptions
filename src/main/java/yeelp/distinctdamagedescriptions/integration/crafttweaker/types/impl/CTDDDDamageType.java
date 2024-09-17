package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import java.util.Map;

import com.google.common.collect.Maps;

import crafttweaker.CraftTweakerAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class CTDDDDamageType implements ICTDDDDamageType {

	private final DDDDamageType type;
	
	private static final Map<DDDDamageType, ICTDDDDamageType> POOL = Maps.newHashMap();
	
	public static final ICTDDDDamageType getFromString(String identifier) {
		DDDDamageType type = DDDRegistries.damageTypes.get("ddd_"+identifier);
		if(type == null || type == DDDBuiltInDamageType.UNKNOWN) {
			CraftTweakerAPI.logError("Can not identify Damage Type: <dddtype:"+identifier+">");
		}
		return getFromDamageType(type);
	}
	
	public static final ICTDDDDamageType getFromDamageType(DDDDamageType type) {
		return POOL.computeIfAbsent(type, CTDDDDamageType::new);
	}
	
	private CTDDDDamageType(DDDDamageType type) {
		this.type = type;
	}
	
	@Override
	public String getInternalName() {
		return this.type.getTypeName();
	}

	@Override
	public String getName() {
		return this.type.getDisplayName();
	}

	@Override
	public String getType() {
		return this.type.getType().name();
	}

	@Override
	public ICTDamageDistribution getDistribution() {
		return new CTDamageDistribution(this.type.getBaseDistribution());
	}
	
	@Override
	public void hideType() {
		this.type.hideType();
	}
	
	@Override
	public void unhideType() {
		this.type.unhideType();
	}

	@Override
	public DDDDamageType asDDDDamageType() {
		return this.type;
	}
	
}
