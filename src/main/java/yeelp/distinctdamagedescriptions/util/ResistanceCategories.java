package yeelp.distinctdamagedescriptions.util;

/**
 * A simple container for storing resistances
 * @author Yeelp
 *
 */
public abstract class ResistanceCategories
{
	private ComparableTriple<Float, Float, Float> resistances;
	private ComparableTriple<Boolean, Boolean, Boolean> immunities;
	
	/**
	 * Create new resistances
	 * @param slashing slashing resistance. Ideally in range (-infty, 1]
	 * @param piercing piercing resistance. Ideally in range (-infty, 1]
	 * @param bludgeoning bludgeoning resistance. Ideally in range (-infty, 1]
	 * @param slashImmune slashing immunity
	 * @param pierceImmune piercing immunity
	 * @param bludgeImmune bludgeoning immunity
	 */
	public ResistanceCategories(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune)
	{
		this.resistances = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
		this.immunities = new ComparableTriple<Boolean, Boolean, Boolean>(slashImmune, pierceImmune, bludgeImmune);
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
	
	/**
	 * Get slashing immunity
	 * @return slashing immunity status
	 */
	public boolean getSlashingImmunity()
	{
		return immunities.getLeft();
	}
	
	/**
	 * Get piercing immunity
	 * @return piercing immunity status
	 */
	public boolean getPiercingImmunity()
	{
		return immunities.getMiddle();
	}
	
	/**
	 * Get bludgeoning immunity
	 * @return bludgeoning immunity status
	 */
	public boolean getBludgeoningImmunity()
	{
		return immunities.getRight();
	}
}
