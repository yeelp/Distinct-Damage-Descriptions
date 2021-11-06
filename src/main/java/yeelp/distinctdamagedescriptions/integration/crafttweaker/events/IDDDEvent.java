package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.damage.IDamageSource;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.IEntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stanhebben.zenscript.ZenRuntimeException;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ddd.events.DDDEvent")
@ZenRegister
/**
 * Base CraftTweaker DistinctDamageDescription Event <br>
 * This event extends IEntityEvent. Use IEntityEvent's getEntity() to get the
 * attacking Entity
 * 
 * @author Yeelp
 *
 */
public interface IDDDEvent extends IEntityEvent {
	/**
	 * Get the defender
	 * 
	 * @return the IEntityLivingBase for the defending entity
	 */
	@ZenGetter("defender")
	IEntityLivingBase getDefender();

	/**
	 * Get the attacker. Merely syntactic sugar for those who want more readable
	 * scripts and wish to distinguish attacker and defender as such. Merely calls
	 * {@link IEntityEvent#getEntity()}
	 * 
	 * @return The IEntity attacker
	 */
	@ZenGetter("attacker")
	default IEntity getAttacker() {
		return this.getEntity();
	}

	/**
	 * Gets the true attacker. The shulker, not the bullet.
	 * 
	 * @return The true attacker. May be null.
	 */
	@ZenGetter("trueAttacker")
	IEntity getTrueAttacker();

	/**
	 * Get the original damage source the defender was hit by.
	 * 
	 * @return the original damage source
	 */
	@ZenGetter("originalSource")
	IDamageSource getOriginalSource();

	/**
	 * Check if this event is {@link Cancelable}. Trying to cancel events that can't
	 * be canceled will throw an exception.
	 * 
	 * @return True if the event can be canceled, false otherwise.
	 */
	@ZenGetter("cancelable")
	default boolean isCancelable() {
		return false;
	}

	/**
	 * Set the canceled status of this event. Further ZenScript listeners will not
	 * be able to react to the event.
	 * 
	 * @param status The cancellation status
	 */
	@ZenMethod("setCanceled")
	default void setCanceled(boolean status) {
		throw new ZenRuntimeException("Can't attempt to cancel event of type: " + this.getClass().getSimpleName());
	}
}
