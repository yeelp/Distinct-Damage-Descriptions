package yeelp.distinctdamagedescriptions.util;

/**
 * Mob Resistances. Used on startup.
 * @author Yeelp
 *
 */
public class MobResistanceCategories extends ResistanceCategories
{
	private float adaptive;
	private float adaptiveAmount;
	/**
	 * Construct a new MobResistanceCategories
	 * @param slashing slashing resistance
	 * @param piercing piercing resistance
	 * @param bludgeoning bludgeoning resistance
	 * @param slashImmune slashing immunity
	 * @param pierceImmune piercing immunity
	 * @param bludgeImmune bludgeoning immunity
	 * @param adaptive adaptability chance
	 */
	public MobResistanceCategories(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, float adaptive, float adaptiveAmount)
	{
		super(slashing, piercing, bludgeoning, slashImmune, pierceImmune, bludgeImmune);
		this.adaptive = adaptive;
	}
	
	/**
	 * Get adaptability status
	 * @return adaptability status
	 */
	public float adaptiveChance()
	{
		return adaptive;
	}
	
	public float getAdaptiveAmount()
	{
		return adaptiveAmount;
	}
}
