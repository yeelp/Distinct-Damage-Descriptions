package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * DDD events for damage calculations. Includes a method to get damage inflicted
 * of a certain type.
 * 
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.DDDCalculationEvent")
@ZenRegister
public interface IDDDCalculationEvent extends IDDDEvent {

	/**
	 * Get damage inflicted of a certain type
	 * 
	 * @param type type name. Requires ddd_ prefix
	 * @return the damage inflicted of that type.
	 */
	@ZenMethod("getDamage")
	float getDamage(String type);
}
