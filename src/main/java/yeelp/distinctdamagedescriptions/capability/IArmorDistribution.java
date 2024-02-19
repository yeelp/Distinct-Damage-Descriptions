package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;

/**
 * Armor resistances
 * 
 * @author Yeelp
 *
 */
public interface IArmorDistribution extends IDistribution {
	/**
	 * Distribute armor points into damage categories
	 * 
	 * @param armor
	 * @param toughness
	 * @return an ArmorMap with the distributed armor and toughness
	 */
	ArmorMap distributeArmor(float armor, float toughness);

	@Override
	IArmorDistribution copy();

	@Override
	IArmorDistribution update(ItemStack owner);

	static void register() {
		DDDCapabilityBase.register(IArmorDistribution.class, NBTTagList.class, ArmorDistribution::new);
	}
}
