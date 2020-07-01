package yeelp.distinctdamagedescriptions.util;

public class BaseDamage
{
	private ComparableTriple<Float, Float, Float> damage;
	/**
	 * Create a new base resistances
	 * @param slashing slashing resistance. Ideally in range (-infty, 1]
	 * @param piercing piercing resistance. Ideally in range (-infty, 1]
	 * @param bludgeoning bludgeoning resistance. Ideally in range (-infty, 1]
	 */
	public BaseDamage(float slashing, float piercing, float bludgeoning)
	{
		this.damage = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
	}
	
	/**
	 * Get slashing resistance
	 * @return slashing resistance
	 */
	public float getSlashingDamage()
	{
		return this.damage.getLeft();
	}
	
	/**
	 * Get piercing resistance
	 * @return piercing resistance
	 */
	public float getPiercingDamage()
	{
		return this.damage.getMiddle();
	}
	
	/**
	 * Get bludgeoning resistance
	 * @return bludgeoning resistance
	 */
	public float getBludgeoningDamage()
	{
		return this.damage.getRight();
	}
}
