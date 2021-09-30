package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;

/**
 * Damage Distribution capability.
 * <p>
 * INVARIANT: all weights will always add to 1.
 * 
 * @author Yeelp
 *
 */
public interface IDamageDistribution extends IDistribution {
	/**
	 * Distribute damage across all categories
	 * 
	 * @param dmg damage
	 * @return a DamageMap with {@code dmg} distributed across all categories
	 */
	DamageMap distributeDamage(float dmg);
	
	static void register() {
		DDDCapabilityBase.register(IDamageDistribution.class, NBTTagList.class, DamageDistribution::new);
	}
}
