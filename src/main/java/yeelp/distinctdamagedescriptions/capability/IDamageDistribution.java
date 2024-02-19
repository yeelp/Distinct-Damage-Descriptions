package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

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

	@Override
	IDamageDistribution copy();

	@Override
	IDamageDistribution update(ItemStack owner);

	/**
	 * Update this capability with respect to its owner
	 * 
	 * @param owner
	 * @return the mutated capability
	 */
	IDamageDistribution update(EntityLivingBase owner);

	/**
	 * Update this capability with respect to its owner
	 * 
	 * @param owner
	 * @return the mutated capability
	 */
	IDamageDistribution update(IProjectile owner);

	static void register() {
		DDDCapabilityBase.register(IDamageDistribution.class, NBTTagList.class, DamageDistribution::new);
	}
}
