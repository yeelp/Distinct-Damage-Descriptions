package yeelp.distinctdamagedescriptions.util;

/**
 * Armor resistances
 * @author Yeelp
 *
 */
public interface IArmorDistribution extends IDistribution
{
	/**
	 * Distribute armor points into three categories
	 * @param armor
	 * @param toughness
	 * @return an ArmorCategories with the distributed armor and toughness
	 */
	ArmorCategories distributeArmor(float armor, float toughness);
}
