package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.IEntityEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

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
	 * @param type the damage type.
	 * @return damage inflicted
	 */
	@ZenMethod
	float getDamage(String type);
	
	/**
	 * Set the damage to inflict via this event
	 * @param type the damage type.
	 * @param damage new damage to inflict
	 */
	@ZenMethod
	void setDamage(String type, float damage);
	
	/**
	 * Get the resistance the defender has against a damage type.
	 * @param type the damage type.
	 * @return the resistance
	 */
	@ZenMethod
	float getResistance(String type);
	
	/**
	 * Set the resistance the defender will use for a damage type
	 * @param type the damage type
	 * @param resistance the new resistance
	 */
	@ZenMethod
	void setResistance(String type, float resistance);
	
	/**
	 * Get the armor value versus a certain type
	 * @param type the damage type
	 * @return the armor amount
	 */
	@ZenMethod
	float getArmor(String type);
	
	/**
	 * Set the armor amount for a certain type
	 * @param type the damage type
	 * @param armor the armor amount.
	 */
	@ZenMethod
	void setArmor(String type, float armor);
	
	@ZenMethod
	float getToughness(String type);
	
	@ZenMethod
	void setToughness(String type, float toughness);
}
