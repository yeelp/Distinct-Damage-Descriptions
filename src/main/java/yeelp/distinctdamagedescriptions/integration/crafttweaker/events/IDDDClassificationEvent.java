package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

/**
 * Events used by DDD for classification during damage calculation
 * 
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.DDDClassificationEvent")
@ZenRegister
public interface IDDDClassificationEvent extends IDDDEvent {

}
