package yeelp.distinctdamagedescriptions.integration.tic.conarm.capability.distributors;

import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.ArmorDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.capability.ConarmArmorDistribution;

public class ConarmArmorDistributor extends AbstractCapabilityDistributor<ItemStack, IArmorDistribution, IArmorDistribution> {
	private static ConarmArmorDistributor instance;

	public ConarmArmorDistributor() {
		super(ArmorDistributionCapabilityDistributor.getInstance());
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof TinkersArmor;
	}

	@Override
	protected IArmorDistribution getCapability(ItemStack t, String key) {
		return new ConarmArmorDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IArmorDistribution> getConfig() {
		return null;
	}

	public static ConarmArmorDistributor getInstance() {
		return instance == null ? instance = new ConarmArmorDistributor() : instance;
	}
}
