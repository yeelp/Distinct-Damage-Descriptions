package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;

/**
 * Events used by DDD for classification during damage calculation
 * 
 * @author Yeelp
 *
 */
@ZenClass(CTConsts.CTClasses.EVENTCLASSIFY)
@ZenRegister
public interface IDDDClassificationEvent extends IDDDEvent {
	//root
}
