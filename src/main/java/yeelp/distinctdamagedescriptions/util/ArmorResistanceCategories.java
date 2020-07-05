package yeelp.distinctdamagedescriptions.util;

public class ArmorResistanceCategories extends ResistanceCategories
{
	private float toughness;
	
	/**
	 * Construct a new ArmorResistanceCategories
	 * @param slashing slashing resistance
	 * @param piercing piercing resistance
	 * @param bludgeoning bludgeoning resistance
	 * @param slashImmune slashing immunity
	 * @param pierceImmune piercing immunity
	 * @param bludgeImmune bludgeoning immunity
	 * @param toughness toughness rating
	 */
	public ArmorResistanceCategories(float slashing, float piercing, float bludgeoning, float toughness)
	{
		super(slashing, piercing, bludgeoning, false, false, false);
		this.toughness = toughness;
	}

	/**
	 * Get toughness rating
	 * @return toughness rating
	 */
	public float getToughnessRating()
	{
		return toughness;
	}
}
