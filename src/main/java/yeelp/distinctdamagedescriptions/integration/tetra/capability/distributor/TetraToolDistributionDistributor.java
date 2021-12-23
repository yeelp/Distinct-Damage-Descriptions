package yeelp.distinctdamagedescriptions.integration.tetra.capability.distributor;

import net.minecraft.item.ItemStack;
import se.mickelus.tetra.items.duplex_tool.ItemDuplexToolModular;
import se.mickelus.tetra.items.sword.ItemSwordModular;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.tetra.capability.TetraToolDistribution;

public final class TetraToolDistributionDistributor extends AbstractCapabilityDistributor<ItemStack, IDamageDistribution, IDamageDistribution> {

	private static TetraToolDistributionDistributor instance;

	protected TetraToolDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForItem.getInstance());
	}

	@Override
	public boolean isApplicable(ItemStack t) {
		return t.getItem() instanceof ItemSwordModular || t.getItem() instanceof ItemDuplexToolModular;
	}

	@Override
	protected IDamageDistribution getCapability(ItemStack t, String key) {
		if(t.getItem() instanceof ItemSwordModular || t.getItem() instanceof ItemDuplexToolModular) {
			return new TetraToolDistribution().update(t);
		}
		return null;
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}

	public static TetraToolDistributionDistributor getInstance() {
		return instance == null ? instance = new TetraToolDistributionDistributor() : instance;
	}
}
