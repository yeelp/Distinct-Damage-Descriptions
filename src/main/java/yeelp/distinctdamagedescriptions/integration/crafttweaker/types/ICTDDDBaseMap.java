package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Map;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.IterableMap;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMemberGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("mods.ddd.lib.IDDDBaseMap")
@IterableMap(key = "mods.ddd.IDDDDamageType", value = "float")
@ZenRegister
public interface ICTDDDBaseMap extends Map<ICTDDDDamageType, Float>{

	@ZenMemberGetter
	@ZenOperator(OperatorType.INDEXGET)
	@ZenMethod
	float get(ICTDDDDamageType type);
	
	@ZenOperator(OperatorType.CONTAINS)
	@ZenMethod
	boolean contains(ICTDDDDamageType type);
}
