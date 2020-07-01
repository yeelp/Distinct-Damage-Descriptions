package yeelp.distinctdamagedescriptions.util;

/**
 * A simple container for storing base resistances. Used on startup to parse config to hold the 3 base resistances
 * @author Yeelp
 *
 */
public class BaseResistances
{
	private ComparableTriple<Float, Float, Float> resistances;
	/**
	 * Create a new base resistances
	 * @param slashing slashing resistance. Ideally in range (-infty, 1]
	 * @param piercing piercing resistance. Ideally in range (-infty, 1]
	 * @param bludgeoning bludgeoning resistance. Ideally in range (-infty, 1]
	 */
	public BaseResistances(float slashing, float piercing, float bludgeoning)
	{
		this.resistances = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
	}
	
	/**
	 * Get slashing resistance
	 * @return slashing resistance
	 */
	public float getSlashingResistance()
	{
		return this.resistances.getLeft();
	}
	
	/**
	 * Get piercing resistance
	 * @return piercing resistance
	 */
	public float getPiercingResistance()
	{
		return this.resistances.getMiddle();
	}
	
	/**
	 * Get bludgeoning resistance
	 * @return bludgeoning resistance
	 */
	public float getBludgeoningResistance()
	{
		return this.resistances.getRight();
	}
}
