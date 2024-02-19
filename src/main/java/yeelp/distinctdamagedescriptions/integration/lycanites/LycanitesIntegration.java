package yeelp.distinctdamagedescriptions.integration.lycanites;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesEquipmentDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesProjectileDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors.LycanitesDamageDistributionDistributor;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors.LycanitesProjectileDistributionDistributor;
import yeelp.distinctdamagedescriptions.integration.lycanites.client.LycanitesSpawnItemResistancesFormatter;
import yeelp.distinctdamagedescriptions.integration.lycanites.client.LycantiesCreatureSpawnItemDamageFormatter;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.LycanitesFireDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.LycanitesFluidDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.LycanitesMinionDistribution;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.SmitedDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public class LycanitesIntegration implements IModIntegration {

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.LYCANITES_ID;
	}
	
	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.LYCANITES_TITLE;
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
		// The cast here helps prevent a BootstrapMethodError since by default, the
		// generic Iterators.forArray gives an
		// Iterator<AbstractCapabilityTooltipFormatter> which is incompatible with
		// TooltipDistributor::registerModCompat
		if(evt.getSide() == Side.CLIENT) {
			Iterators.forArray((IModCompatTooltipFormatter<ItemStack>) LycanitesSpawnItemResistancesFormatter.getInstance(), LycantiesCreatureSpawnItemDamageFormatter.getInstance()).forEachRemaining(TooltipDistributor::registerModCompat);	
		}
		DDDRegistries.distributions.registerAll(new SmitedDistribution(), new LycanitesMinionDistribution(), LycanitesFireDistribution.DOOMFIRE, LycanitesFireDistribution.FROSTFIRE, LycanitesFireDistribution.HELLFIRE, LycanitesFireDistribution.ICEFIRE, LycanitesFireDistribution.PRIMEFIRE, LycanitesFireDistribution.SCORCHFIRE, LycanitesFireDistribution.SHADOWFIRE, LycanitesFireDistribution.SMITEFIRE, LycanitesFluidDistribution.ACID, LycanitesFluidDistribution.OOZE);
		return IModIntegration.super.init(evt);
	}
}
