package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

/**
 * Event fired when DDD tries to determine damage distributions.
 * 
 * Listeners can add additional damage here. This added damage won't be
 * reflected in tooltips, it's just present for this calculation.
 * 
 * Basically, use this event to give conditional damage bonuses.
 * 
 * @author Yeelp
 *
 */
@ZenClass(CTConsts.CTClasses.EVENTDETERMINEDAMAGE)
@ZenRegister
public interface IDetermineDamageEvent extends IDDDClassificationEvent {

	/**
	 * Get damage for a certain type
	 * 
	 * @param type Type name. Requires ddd_ prefix
	 * @return The damage inflicted of that type
	 */
	@ZenMethod("getDamage")
	float getDamage(ICTDDDDamageType type);

	/**
	 * Set damage inflicted for a certain type
	 * 
	 * @param type   Type name. Requires ddd_ prefix
	 * @param amount Amount of damage to inflict
	 */
	@ZenMethod("setDamage")
	void setDamage(ICTDDDDamageType type, float amount);
}
