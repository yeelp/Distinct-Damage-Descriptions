package yeelp.distinctdamagedescriptions.capability;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;

/**
 * A wrapped DamageResistance with additional information about mob adaptive
 * immunity.
 * 
 * @author Yeelp
 *
 */
public interface IMobResistances extends IDamageResistances {
	/**
	 * Does this mob have adaptive resistance?
	 * 
	 * @return true if they do, false if they don't.
	 */
	boolean hasAdaptiveResistance();

	/**
	 * Get the amount of bonus resistance applied when adaptability triggers.
	 * 
	 * @return the bonus resistance for adaptability.
	 */
	float getAdaptiveAmount();

	/**
	 * Set the adaptive resistance amount.
	 * 
	 * @param amount
	 */
	void setAdaptiveAmount(float amount);

	/**
	 * Set the adaptive resistance status.
	 * 
	 * @param status true if this mob should be adaptive resistant, false if not.
	 */
	void setAdaptiveResistance(boolean status);

	/**
	 * Update adaptive resistance. Doesn't check if this mob is adaptive.
	 * 
	 * @param damageTypes new types this mob should be more resistant to.
	 * @return true if there was a net change in any resistances
	 */
	boolean updateAdaptiveResistance(DDDDamageType... damageTypes);
}
