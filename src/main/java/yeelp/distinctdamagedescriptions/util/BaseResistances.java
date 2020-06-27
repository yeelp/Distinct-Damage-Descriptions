package yeelp.distinctdamagedescriptions.util;

/**
 * A simple container for storing base resistances. Used on startup to parse config to hold the 3 base resistances
 * @author Yeelp
 *
 */
public class BaseResistances
{
	private float slashing;
	private float piercing;
	private float bludgeoning;
	/**
	 * Create a new base resistances
	 * @param slashing slashing resistance. Ideally in range (-infty, 1]
	 * @param piercing piercing resistance. Ideally in range (-infty, 1]
	 * @param bludgeoning bludgeoning resistance. Ideally in range (-infty, 1]
	 */
	public BaseResistances(float slashing, float piercing, float bludgeoning)
	{
		this.slashing = slashing;
		this.piercing = piercing;
		this.bludgeoning = bludgeoning;
	}
	
	/**
	 * Get slashing resistance
	 * @return slashing resistance
	 */
	public float getSlashingResistance()
	{
		return this.slashing;
	}
	
	/**
	 * Get piercing resistance
	 * @return piercing resistance
	 */
	public float getPiercingResistance()
	{
		return this.piercing;
	}
	
	/**
	 * Get bludgeoning resistance
	 * @return bludgeoning resistance
	 */
	public float getBludgeoningResistance()
	{
		return this.bludgeoning;
	}
}
