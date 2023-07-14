package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType.Type;
import yeelp.distinctdamagedescriptions.api.impl.DDDCustomDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class CoTDDDDamageTypeBuilder {

	@ZenProperty
	public String name;
	
	@ZenProperty
	public String displayName;
	
	@ZenProperty
	public String deathMessageNoAttacker;
	
	@ZenProperty
	public String deathMessageHasAttacker;
	
	@ZenProperty
	public int color;
	
	private Type type = Type.SPECIAL;
	
	@ZenMethod
	public void setPhysical() {
		this.type = Type.PHYSICAL;
	}
	
	@ZenMethod
	public void setSpecial() {
		this.type = Type.SPECIAL;
	}
	
	@ZenMethod
	public ICTDDDDamageType register() {
		DDDDamageType type = new DDDCustomDamageType(this.name, this.displayName, this.type == Type.PHYSICAL, this.deathMessageHasAttacker, this.deathMessageNoAttacker, this.color);
		DDDRegistries.damageTypes.register(type);
		return CTDDDDamageType.getFromDamageType(type);
	}
}
