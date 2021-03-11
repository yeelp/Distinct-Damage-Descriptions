package yeelp.distinctdamagedescriptions.api;

import javax.annotation.Nullable;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

/**
 * Basic skeleton of a DDD damage type.
 * @author Yeelp
 *
 */
public interface DDDDamageType
{
	public enum Type
	{
		PHYSICAL,
		SPECIAL;
	}
	/**
	 * Get the name of this type, with the "ddd_" prefix
	 * @return the type name, with the "ddd_" prefix.
	 */
	public String getTypeName();
	
	/**
	 * Get the display name for this DDDDamageType
	 * @return the display name
	 */
	public String getDisplayName();
	
	/**
	 * Get the base damage distribution for this type
	 * @return and IDistribution that distributes the damage to the type returned by {@link #getTypeName()}
	 */
	public IDamageDistribution getBaseDistribution();
	
	/**
	 * Get the type of this damage
	 * @return a Type enum, that is either PHYSICAL or SPECIAL
	 */
	public Type getType();
	
	/**
	 * Get the death message for this damage type
	 * @param hasAttacker true if the damage type had an attacker
	 * @return the corresponding death message
	 */
	public @Nullable String getDeathMessage(boolean hasAttacker);
	
	/**
	 * Is this damage type a custom defined one, or built in?
	 * @return true if user defined, false if built in.
	 */
	public boolean isCustomDamage();
}


