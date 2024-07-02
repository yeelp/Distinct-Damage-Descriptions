package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.List;
import java.util.stream.Collectors;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

@ZenClass(CTConsts.CTClasses.CTDAMAGETYPE)
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
	
	@ZenMethod
	static List<ICTDDDDamageType> getAllTypes() {
		return DDDRegistries.damageTypes.getAll().stream().map(CTDDDDamageType::getFromDamageType).collect(Collectors.toList());
	}
	
	DDDDamageType asDDDDamageType();
}
