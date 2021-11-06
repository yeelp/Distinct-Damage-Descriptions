package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Fired when a mob decides if it should update adaptive resistances.
 * 
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.UpdateAdaptiveResistanceEvent")
@ZenRegister
public interface IUpdateAdaptiveResistances extends IDDDCalculationEvent {

	/**
	 * Get the entity's current resistance to a type
	 * 
	 * @param type Type name. Requires ddd_ prefix
	 * @return this entity's resistance to that type.
	 */
	@ZenMethod("getResistance")
	float getResistance(String type);

	/**
	 * Check if this entity is immune to a type
	 * 
	 * @param type Type name. Requires ddd_ prefix
	 * @return true if this entity is immune to this type.
	 */
	@ZenMethod("hasImmunity")
	boolean hasImmunity(String type);

	/**
	 * Prevent this entity from adapting to this type
	 * 
	 * @param type Type name. Requires ddd_ prefix
	 */
	@ZenMethod("ignoreType")
	void ignoreType(String type);

	/**
	 * Prevent this entity from updating it's adaptive resistances. Will stick with
	 * whatever types it had adapted to previously, if any.
	 */
	@ZenMethod("denyResult")
	default void preventAdaptiveUpdate() {
		this.setResult(Result.DENY);
	}

	/**
	 * Only let this entity update it's adaptive resistances if it's actually
	 * adaptive.
	 */
	@ZenMethod("defaultResult")
	default void restoreDefaultAction() {
		this.setResult(Result.DEFAULT);
	}

	/**
	 * Force this mob to update it's adaptive resistances, even if it doesn't have
	 * it. Setting this doesn't give the mob adaptive resistances permanently. It
	 * only updates it this one time. Mobs without adaptive resistances will keep
	 * these new resistance bonuses until they are forced to update again.
	 */
	@ZenMethod("forceResult")
	default void forceAdaptiveUpdate() {
		this.setResult(Result.ALLOW);
	}

	/**
	 * Set this event's result
	 * @param result the result.
	 */
	void setResult(Result result);
}
