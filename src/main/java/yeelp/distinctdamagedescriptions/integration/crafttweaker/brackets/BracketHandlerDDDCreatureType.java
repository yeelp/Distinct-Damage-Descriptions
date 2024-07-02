package yeelp.distinctdamagedescriptions.integration.crafttweaker.brackets;

import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTCreatureTypeDefinition;

@BracketHandler
@ZenRegister
public final class BracketHandlerDDDCreatureType extends AbstractDDDBracketHandler {

	public BracketHandlerDDDCreatureType() {
		super("dddcreaturetype", CTCreatureTypeDefinition.class, CTConsts.CTStrings.GET_FROM_STRING_METHOD);
	}
}
