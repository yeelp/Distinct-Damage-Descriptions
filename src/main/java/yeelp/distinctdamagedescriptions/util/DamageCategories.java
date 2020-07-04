package yeelp.distinctdamagedescriptions.util;

public class DamageCategories
{
	private ComparableTriple<Float, Float, Float> damage;
	/**
	 * Create a new base damage
	 * @param slashing slashing damage.
	 * @param piercing piercing damage.
	 * @param bludgeoning bludgeoning damage.
	 */
	public DamageCategories(float slashing, float piercing, float bludgeoning)
	{
		this.damage = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
	}
	
	/**
	 * Get slashing damage
	 * @return slashing damage
	 */
	public float getSlashingDamage()
	{
		return this.damage.getLeft();
	}
	
	/**
	 * Get piercing damage
	 * @return piercing damage
	 */
	public float getPiercingDamage()
	{
		return this.damage.getMiddle();
	}
	
	/**
	 * Get bludgeoning damage
	 * @return bludgeoning damage
	 */
	public float getBludgeoningDamage()
	{
		return this.damage.getRight();
	}
}
