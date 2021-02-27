package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;

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
	 * @return a Map<String, Float> with {@code dmg} distributed across all categories
	 */
	Map<String, Float> distributeDamage(float dmg);
}