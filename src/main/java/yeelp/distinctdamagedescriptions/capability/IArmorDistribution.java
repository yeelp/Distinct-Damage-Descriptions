package yeelp.distinctdamagedescriptions.capability;

import yeelp.distinctdamagedescriptions.util.ArmorMap;

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
	 * @return an ArmorMap with the distributed armor and toughness
	 */
	ArmorMap distributeArmor(float armor, float toughness);
}
