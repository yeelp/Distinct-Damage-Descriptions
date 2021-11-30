package yeelp.distinctdamagedescriptions.integration.lycanites;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesEquipmentDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesProjectileDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors.LycanitesDamageDistributionDistributor;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors.LycanitesProjectileDistributionDistributor;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.SmitedDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class LycanitesIntegration implements IModIntegration {

	@Override
	public String getModID() {
		return ModConsts.LYCANITES_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new FontRendererHandler());
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		DDDCapabilityDistributors.addItemCap(LycanitesDamageDistributionDistributor.getInstance());
		DDDCapabilityDistributors.addProjCap(LycanitesProjectileDistributionDistributor.getInstance());
		LycanitesEquipmentDistribution.register();
		LycanitesProjectileDistribution.register();
		DDDRegistries.distributions.register(new SmitedDistribution());
		return IModIntegration.super.init(evt);
	}
}
