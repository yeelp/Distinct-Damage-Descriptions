package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.distributors;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerDamageDistribution;

public final class TinkerToolCapabilityDistributor extends AbstractCapabilityDistributor<ItemStack, IDamageDistribution, IDamageDistribution> {

	private static TinkerToolCapabilityDistributor instance;
	
	protected TinkerToolCapabilityDistributor() {
		super(DamageDistributionCapabilityDistributor.ForItem.getInstance());
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof TinkerToolCore;
	}

	@Override
	protected IDamageDistribution getCapability(ItemStack t, String key) {
		return new TinkerDamageDistribution(this.getConfig().get(key)).update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return DDDConfigurations.items;
	}
	
	public static TinkerToolCapabilityDistributor getInstance() {
		return instance == null ? instance = new TinkerToolCapabilityDistributor() : instance;
	}
}
