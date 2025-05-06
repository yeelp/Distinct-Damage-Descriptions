package yeelp.distinctdamagedescriptions.integration.thaumcraft;

import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.api.aspects.Aspect;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.fermiumbooter.FermiumBootedModIntegration;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.client.FocusDistributionItemFormatter;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.client.FocusItemDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.dist.ThaumcraftFocusDistribution;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.dist.ThaumcraftPredefinedDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public final class ThaumcraftIntegration extends FermiumBootedModIntegration {

	@Override
	public String getModTitle() {
		return IntegrationTitles.THAUMCRAFT_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.THAUMCRAFT_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new Handler() {
			@SubscribeEvent
			public void onConfigChange(ConfigChangedEvent evt) {
				if(evt.getModID().equals(ModConsts.MODID)) {
					ThaumcraftPredefinedDistribution.update();
				}
			}
		});
	}

	@Override
	protected boolean enabled() {
		return ModConfig.compat.thaumcraft.enabled;
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		if(evt.getSide() == Side.CLIENT) {
			Stream.of((IModCompatTooltipFormatter<ItemStack>) FocusDistributionItemFormatter.getInstance(), FocusItemDamageDistributionFormatter.getInstance()).forEach(TooltipDistributor::registerModCompat);
		}
		return super.init(evt);
	}
	
	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		ThaumcraftConfigurations.aspectDists.forEach((entry) -> {
			String name = entry.getKey();
			if(!Aspect.aspects.containsKey(name)) {
				DistinctDamageDescriptions.warn(String.format("The Aspect %s doesn't exist! Maybe it was spelled wrong?", name));
			}
		});
		DDDRegistries.distributions.register(new ThaumcraftFocusDistribution());
		ThaumcraftPredefinedDistribution.getDists().forEach(DDDRegistries.distributions::register);
		return super.postInit(evt);
	}

}
