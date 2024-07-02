package yeelp.distinctdamagedescriptions.integration.crafttweaker.brackets;

import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDDamageType;

@BracketHandler
@ZenRegister
public final class BracketHandlerDDDDamageType extends AbstractDDDBracketHandler {

	public BracketHandlerDDDDamageType() {
		super("dddtype", CTDDDDamageType.class, CTConsts.CTStrings.GET_FROM_STRING_METHOD);
	}
}
