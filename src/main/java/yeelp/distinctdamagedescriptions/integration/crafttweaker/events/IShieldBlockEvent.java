package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

/**
 * Fired whenever a shield attempts to reduce damage. Can be canceled. If
 * canceled, the shield does nothing.
 * 
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.ShieldBlockEvent")
@ZenRegister
public interface IShieldBlockEvent extends IDDDClassificationEvent {

	/**
	 * Get the shield stack being used.
	 * 
	 * @return the shield being used
	 */
	@ZenGetter("shield")
	IItemStack getShield();

	/**
	 * Get the shield's effectiveness for a type
	 * 
	 * @param type type name. Requires ddd_ prefix
	 * @return the shield's effectiveness for that type.
	 */
	@ZenMethod("getEffectiveness")
	float getShieldEffectivenessForType(ICTDDDDamageType type);

	/**
	 * Set the shield's effectiveness for a type
	 * 
	 * @param type   type name. Requires ddd_ prefix
	 * @param amount the shield's effectiveness for that type.
	 */
	@ZenMethod("setEffectiveness")
	void setShieldEffectivenessForType(ICTDDDDamageType type, float amount);

	@Override
	default boolean isCancelable() {
		return true;
	}

	@Override
	void setCanceled(boolean status);

}
