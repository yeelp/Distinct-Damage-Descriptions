package yeelp.distinctdamagedescriptions.util;

/**
 * Wrapped DamageResistances with toughness. An armor with a higher toughness rating is more effective at blocking stronger attacks.
 * @author Yeelp
 *
 */
public interface IArmorResistances extends IDamageResistances
{
	/**
	 * Get the toughness value of this armor
	 * @return the toughness value.
	 */
	float getToughness();
	
	/**
	 * Set the toughness value of this armor.
	 * @param toughness new toughness value.
	 */
	void setToughness(float toughness);
}
