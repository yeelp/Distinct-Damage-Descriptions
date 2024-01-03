package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

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
	 * @param type type name.
	 * @return the damage inflicted of that type.
	 */
	@ZenMethod("getDamage")
	float getDamage(ICTDDDDamageType type);
}
