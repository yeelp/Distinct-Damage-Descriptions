package yeelp.distinctdamagedescriptions.capability.distributors;

import com.lycanitesmobs.core.item.equipment.ItemEquipment;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.impl.LycanitesEquipmentDistribution;

public class LycanitesCapabilityDistributor extends ModCompatCapabilityDistributor<ItemStack, LycanitesEquipmentDistribution> {

	private static LycanitesCapabilityDistributor instance;
	
	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ItemEquipment;
	}

	@Override
	protected LycanitesEquipmentDistribution getCapability(ItemStack t, String key) {
		return new LycanitesEquipmentDistribution();
	}

	public static final LycanitesCapabilityDistributor getInstance() {
		return instance == null ? instance = new LycanitesCapabilityDistributor() : instance;
	}
}
