package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.IEntityEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("mods.ddd.events.DDDEvent")
@ZenRegister
/**
 * Base CraftTweaker DistinctDamageDescription Event
 * <br>
 * This event extends IEntityEvent. Use IEntityEvent's getEntity() to get the attacking Entity
 * @author Yeelp
 *
 */
public abstract interface IDDDEvent extends IEntityEvent
{
	/**
	 * Get the defender
	 * @return the IEntityLivingBase for the defending entity
	 */
	@ZenGetter("defender")
	IEntityLivingBase getDefender();
	
	/**
	 * Get the attacker. Merely syntactic sugar for those who want
	 * more readable scripts and wish to distinguish attacker and defender as such. Merely calls {@link IEntityEvent#getEntity()}
	 * @return
	 */
	@ZenGetter("attacker")
	default IEntity getAttacker()
	{
		return this.getEntity();
	}
	
	/**
	 * Get the damage inflicted
	 * @return damage inflicted
	 */
	@ZenGetter("damage")
	float getDamage();
	
	/**
	 * Set the damage to inflict via this event
	 * @param damage new damage to inflict
	 */
	@ZenSetter("damage")
	void setDamage(float damage);
	
	/**
	 * Get the resistance the defender has against this damage.
	 * @return the resistance
	 */
	@ZenGetter("resistance")
	abstract float getResistance();
	
	/**
	 * Set the resistance the defender will use for this damage
	 * @param resistance the new resistance
	 */
	@ZenSetter("resistance")
	abstract void setResiatance(float resistance);
}
