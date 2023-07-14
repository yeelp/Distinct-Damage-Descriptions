package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

/**
 * Event fired when DDD gather mob defenses, which includes resistances and
 * immunities
 * 
 * @author Yeelp
 *
 */
@ZenClass("mods.ddd.events.GatherDefensesEvent")
@ZenRegister
public interface IGatherDefensesEvent extends IDDDClassificationEvent {

	/**
	 * Get resistance for a certain type
	 * 
	 * @param type type name.
	 * @return resistance for that type. Weakness if less than zero.
	 */
	@ZenMethod("getResistance")
	float getResistance(ICTDDDDamageType type);

	/**
	 * Set resistance for a type. Can set a weakness by passing in a negative
	 * {@code amount}
	 * 
	 * @param type type name.
	 * @param amount
	 */
	@ZenMethod("setResistance")
	void setResistance(ICTDDDDamageType type, float amount);

	/**
	 * Check if this entity has a resistance to this type
	 * 
	 * @param type type name. Requires ddd_prefix
	 * @return true if they have a positive resistance to this type.
	 */
	@ZenMethod("hasResistance")
	default boolean hasResistance(ICTDDDDamageType type) {
		return this.getResistance(type) > 0;
	}

	/**
	 * Check if this entity has a weakness to this type
	 * 
	 * @param type type name. Requires ddd_ prefix
	 * @return true if they have a weakness to this type.
	 */
	@ZenMethod("hasWeakness")
	default boolean hasWeakness(ICTDDDDamageType type) {
		return this.getResistance(type) < 0;
	}

	/**
	 * Check if this entity has immunity to this type.
	 * 
	 * @param type type name. Requires ddd_ prefix
	 * @return true if this entity is immune to this type currently.
	 */
	@ZenMethod("hasImmunity")
	boolean hasImmunity(ICTDDDDamageType type);

	/**
	 * Temporarily grant this entity immunity to the given type. If this entity
	 * already has immunity to this type, nothing happens.
	 * 
	 * @param type type name. Requires ddd_ prefix
	 */
	@ZenMethod("grantImmunity")
	void grantImmunity(ICTDDDDamageType type);

	/**
	 * Temporarily revoke this entity's immunity to the given type. If the entity
	 * doesn't have immunity to this type, nothing happens.
	 * 
	 * @param type
	 */
	@ZenMethod("revokeImmunity")
	void revokeImmunity(ICTDDDDamageType type);

	/**
	 * Clear all immunities this entity has for this calculation
	 */
	@ZenMethod("clearImmunities")
	void clearImmunities();

	/**
	 * clear all resistances and weaknesses this entity has for this calculation
	 */
	@ZenMethod("clearResistancesAndWeaknesses")
	void clearResistances();
}
