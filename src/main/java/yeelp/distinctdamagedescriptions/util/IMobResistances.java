package yeelp.distinctdamagedescriptions.util;

/**
 * A wrapped DamageResistance with additional information about mob adaptive immunity.
 * @author Yeelp
 *
 */
public interface IMobResistances extends IDamageResistances
{
	/**
	 * Does this mob have adaptive immunity?
	 * @return true if they do, false if they don't.
	 */
	boolean hasAdaptiveImmunity();
	
	/**
	 * Set the adaptive immunity status.
	 * @param status true if this mob should be adaptive immune, false if not.
	 */
	void setAdaptiveImmunity(boolean status);
	
	/**
	 * Update adaptive immunity.
	 * @param damageTypes new types this mob should be immune to. If all three are specified, then one DamageType is randomly chosen, and this mob is susceptible to that DamageType.
	 */
	void updateAdaptiveImmunity(DamageType...damageTypes);
}
