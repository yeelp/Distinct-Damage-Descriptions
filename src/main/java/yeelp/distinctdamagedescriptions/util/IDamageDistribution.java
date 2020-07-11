package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Damage Distribution capability. <p>
 * INVARIANT: all weights will always add to 1.
 * @author Yeelp
 *
 */
public interface IDamageDistribution extends IDistribution
{
	/**
	 * Distribute damage across all three categories
	 * @param dmg damage
	 * @return a DamageCategories with {@code dmg} distributed across all three categories
	 */
	DamageCategories distributeDamage(float dmg);
	
}
