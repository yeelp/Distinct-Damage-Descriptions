package yeelp.distinctdamagedescriptions.util;

/**
 * Damage Distribution capability. <p>
 * INVARIANT: all weights will always add to 1.
 * @author Yeelp
 *
 */
public interface IDamageDistribution extends IDistribution
{
	/**
	 * Distribute damage across all categories
	 * @param dmg damage
	 * @return a DamageCategories with {@code dmg} distributed across all categories
	 */
	DamageCategories distributeDamage(float dmg);
	
}
