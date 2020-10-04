package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import java.util.List;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * CraftTweaker Custom Damage Event
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.CustomDamageEvent")
@ZenRegister
public interface ICustomDamageEvent extends IDDDEvent
{
	/**
	 * Get a list of damage types being inflicted by this CustomDamageType
	 * @return
	 */
	@ZenGetter("damageTypes")
	List<String> getDamageTypes();
	
	/**
	 * A convenience ZenMethod for checking if a damage type is present.
	 * @param damageType
	 * @return
	 */
	@ZenMethod
	default boolean hasDamageType(String damageType)
	{
		return getDamageTypes().contains(damageType);
	}
}
