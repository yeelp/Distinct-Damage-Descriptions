package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

/**
 * CraftTweaker Physical Damage Event class
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.PhysicalDamageEvent")
@ZenRegister
public interface IPhysicalDamageEvent extends IDDDEvent
{
	/**
	 * Get the damage type as a String
	 * @return the damage type as a String
	 */
	@ZenGetter("damageType")
	String getDamageType();
}
