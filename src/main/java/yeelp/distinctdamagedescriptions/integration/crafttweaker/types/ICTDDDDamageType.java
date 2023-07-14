package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;

@ZenClass("mods.ddd.IDDDDamageType")
@ZenRegister
public interface ICTDDDDamageType {

	@ZenGetter("internalName")
	String getInternalName();
	
	@ZenGetter("name")
	String getName();
	
	@ZenGetter("type")
	String getType();
	
	@ZenGetter("distribution")
	ICTDamageDistribution getDistribution();
	
	DDDDamageType asDDDDamageType();
}
