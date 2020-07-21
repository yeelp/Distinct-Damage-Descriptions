package yeelp.distinctdamagedescriptions.util;

/**
 * A wrapped DamageResistance with additional information about mob adaptive immunity.
 * @author Yeelp
 *
 */
public interface IMobResistances extends IDamageResistances
{
	/**
	 * Does this mob have adaptive resistance?
	 * @return true if they do, false if they don't.
	 */
	boolean hasAdaptiveResistance();
	
	/**
	 * Get the amount of bonus resistance applied when adaptability triggers.
	 * @return the bonus resistance for adaptability.
	 */
	float getAdaptiveAmount();
	
	/**
	 * Set the adaptive resistance status.
	 * @param status true if this mob should be adaptive resistant, false if not.
	 */
	void setAdaptiveResistance(boolean status);
	
	/**
	 * Update adaptive resistance.
	 * @param damageTypes new types this mob should be more resistant to.
	 * @return true if there was a net change in any resistances
	 */
	boolean updateAdaptiveResistance(DamageType...damageTypes);
}
