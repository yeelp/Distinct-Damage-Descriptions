package yeelp.distinctdamagedescriptions.integration.capability.distributors;

import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;

public abstract class ModCompatCapabilityDistributor<T, C extends IDistributionRequiresUpdate> extends AbstractCapabilityDistributor<T, IDistributionRequiresUpdate, C> {

	protected static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "distributionUpdate");
	protected ModCompatCapabilityDistributor() {
		super(LOC);
	}
	
	protected ModCompatCapabilityDistributor(ResourceLocation loc) {
		super(loc);
	}

	@Override
	protected IDDDConfiguration<IDistributionRequiresUpdate> getConfig() {
		return null;
	}
}
