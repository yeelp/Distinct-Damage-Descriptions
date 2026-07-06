package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.*;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;

import java.util.Map;

@ZenClass(CTConsts.CTClasses.DDDMAP)
@IterableMap(key = CTConsts.CTClasses.CTDAMAGETYPE, value = "float")
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
