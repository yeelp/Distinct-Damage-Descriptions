package yeelp.distinctdamagedescriptions.integration.tetra;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.tetra.capability.TetraToolDistribution;
import yeelp.distinctdamagedescriptions.integration.tetra.capability.distributor.TetraToolDistributionDistributor;

public final class TetraIntegration implements IModIntegration {

	@Override
	public String getModID() {
		return ModConsts.TETRA_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		TetraToolDistribution.register();
		DDDCapabilityDistributors.addItemCap(TetraToolDistributionDistributor.getInstance());
		return IModIntegration.super.init(evt);
	}

}
