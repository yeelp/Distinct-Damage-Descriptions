package yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors;

import com.lycanitesmobs.core.item.equipment.ItemEquipment;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesEquipmentDistribution;

public class LycanitesDamageDistributionDistributor extends AbstractCapabilityDistributor<ItemStack, IDamageDistribution, IDamageDistribution>{

	private static LycanitesDamageDistributionDistributor instance;
	
	protected LycanitesDamageDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForItem.getInstance());
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ItemEquipment;
	}

	@Override
	protected IDamageDistribution getCapability(ItemStack t, String key) {
		return new LycanitesEquipmentDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}
	
	public static LycanitesDamageDistributionDistributor getInstance() {
		return instance == null ? instance = new LycanitesDamageDistributionDistributor() : instance;
	}

}
