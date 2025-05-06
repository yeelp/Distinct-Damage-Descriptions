package yeelp.distinctdamagedescriptions.integration.bettersurvival;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.mujmajnkraft.bettersurvival.items.ItemSpear;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.BetterSurvivalLinkedThrownSpearDistribution;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.BetterSurvivalThrownSpearDistribution;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.distributor.BetterSurvivalThrownSpearDistributionDistributor;

public final class BetterSurvivalCompat implements IModIntegration {

	@Override
	public String getModTitle() {
		return IntegrationTitles.BETTER_SURVIVAL_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.BETTER_SURVIVAL_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		BetterSurvivalThrownSpearDistribution.register();
		DDDCapabilityDistributors.addProjCap(BetterSurvivalThrownSpearDistributionDistributor.getInstance());
		return IModIntegration.super.init(evt);
	}
	
	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		Iterators.filter(ForgeRegistries.ITEMS.iterator(), ItemSpear.class).forEachRemaining((spear) -> {
			String s = ForgeRegistries.ITEMS.getKey(spear).toString();
			DistinctDamageDescriptions.debug("Registering projectile pair: "+s);
			DDDConfigurations.projectiles.registerItemProjectilePair(s, s);
			DDDConfigurations.projectiles.put(s, new BetterSurvivalLinkedThrownSpearDistribution(s));
		});
		return IModIntegration.super.postInit(evt);
	}

}
