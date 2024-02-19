package yeelp.distinctdamagedescriptions.integration.spartanweaponry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.oblivioussp.spartanweaponry.api.IWeaponPropertyContainer;
import com.oblivioussp.spartanweaponry.api.WeaponProperties;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability.SpartanThrownWeaponDistribution;
import yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability.distributor.SpartanThrownWeaponDistributionDistributor;

public final class SpartanWeaponryCompat implements IModIntegration {

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.SPARTAN_WEAPONRY_ID;
	}
	
	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.SPARTAN_WEAPONRY_TITLE;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		SpartanThrownWeaponDistribution.register();
		DDDCapabilityDistributors.addProjCap(SpartanThrownWeaponDistributionDistributor.getInstance());
		return IModIntegration.super.init(evt);
	}

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		DistinctDamageDescriptions.debug("Registering projectile pairs for Spartan Weaponry...");
		Iterators.filter(ForgeRegistries.ITEMS.iterator(), IWeaponPropertyContainer.class).forEachRemaining((props) -> {
			if(props.hasWeaponProperty(WeaponProperties.THROWABLE)) {
				String s = ForgeRegistries.ITEMS.getKey((Item) props).toString();
				DistinctDamageDescriptions.debug("Registered projectile pair: " + s);
				DDDConfigurations.projectiles.registerItemProjectilePair(s, s);
				DDDConfigurations.projectiles.put(s, new DamageDistribution());
			}
		});
		return IModIntegration.super.postInit(evt);
	}

}
