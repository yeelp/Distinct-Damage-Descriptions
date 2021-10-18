package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.ToolCore;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

public class TinkerBlankShieldDistributionCapabilityDistributor extends AbstractCapabilityDistributor<ItemStack, ShieldDistribution, ShieldDistribution> {

	private static TinkerBlankShieldDistributionCapabilityDistributor instance;
	
	private TinkerBlankShieldDistributionCapabilityDistributor() {
		super(ShieldDistributionCapabilityDistributor.LOC);
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ToolCore && t.getItemUseAction() == EnumAction.BLOCK;
	}

	@Override
	protected ShieldDistribution getCapability(ItemStack t, String key) {
		return new ShieldDistribution();
	}

	@Override
	protected IDDDConfiguration<ShieldDistribution> getConfig() {
		return null;
	}

	public static TinkerBlankShieldDistributionCapabilityDistributor getInstance() {
		return instance == null ? instance = new TinkerBlankShieldDistributionCapabilityDistributor() : instance;
	}
}
