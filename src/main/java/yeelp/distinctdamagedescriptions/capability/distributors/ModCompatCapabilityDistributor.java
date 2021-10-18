package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

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
