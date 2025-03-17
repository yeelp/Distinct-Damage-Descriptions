package yeelp.distinctdamagedescriptions.integration.baubles;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.baubles.client.BaubleModifierFormatter;
import yeelp.distinctdamagedescriptions.integration.baubles.client.BaublesItemDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.integration.baubles.handler.BaublesHandler;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public final class BaublesIntegration implements IModIntegration {

	@Override
	public String getModTitle() {
		return IntegrationTitles.BAUBLES_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.BAUBLES_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new BaublesHandler());
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		TooltipDistributor.registerModCompat(BaubleModifierFormatter.getInstance());
		TooltipDistributor.registerModCompat(BaublesItemDamageDistributionFormatter.getInstance());
		return true;
	}

}
