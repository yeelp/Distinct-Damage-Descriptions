package yeelp.distinctdamagedescriptions.capability.distributors;

import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

public class ConarmBlankArmorDistributionCapabilityDistributor extends AbstractCapabilityDistributor<ItemStack, IArmorDistribution, IArmorDistribution> {

	private static ConarmBlankArmorDistributionCapabilityDistributor instance;
	
	private ConarmBlankArmorDistributionCapabilityDistributor() {
		super(ArmorDistributionCapabilityDistributor.LOC);
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof TinkersArmor;
	}

	@Override
	protected IArmorDistribution getCapability(ItemStack t, String key) {
		return new ArmorDistribution();
	}

	@Override
	protected IDDDConfiguration<IArmorDistribution> getConfig() {
		return null;
	}
	
	public static ConarmBlankArmorDistributionCapabilityDistributor getInstance() {
		return instance == null ? instance = new ConarmBlankArmorDistributionCapabilityDistributor() : instance;
	}

}
