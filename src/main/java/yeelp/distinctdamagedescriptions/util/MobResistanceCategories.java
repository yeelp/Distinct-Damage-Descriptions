package yeelp.distinctdamagedescriptions.util;

/**
 * Mob Resistances. Used on startup.
 * @author Yeelp
 *
 */
public class MobResistanceCategories extends ResistanceCategories
{
	private boolean adaptive;
	/**
	 * Construct a new MobResistanceCategories
	 * @param slashing slashing resistance
	 * @param piercing piercing resistance
	 * @param bludgeoning bludgeoning resistance
	 * @param slashImmune slashing immunity
	 * @param pierceImmune piercing immunity
	 * @param bludgeImmune bludgeoning immunity
	 * @param adaptive adaptability status
	 */
	public MobResistanceCategories(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, boolean adaptive)
	{
		super(slashing, piercing, bludgeoning, slashImmune, pierceImmune, bludgeImmune);
		this.adaptive = adaptive;
	}
	
	/**
	 * Get adaptability status
	 * @return adaptability status
	 */
	public boolean isAdaptive()
	{
		return adaptive;
	}
}
