package yeelp.distinctdamagedescriptions.integration.electroblobswizardry;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public class ElectroblobsWizardryIntegration implements IModIntegration {

	@Override
	public String getModTitle() {
		return IntegrationTitles.WIZARDRY_NAME;
	}

	@Override
	public String getModID() {
		return IntegrationIds.WIZARDRY_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		return IModIntegration.super.init(evt);
	}
	
	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		WizardryConfigurations.minionResistancesReference.forEach((entry) -> {
			if(DDDConfigurations.mobResists.configured(entry.getKey())) {
				return;
			}
			DDDConfigurations.mobResists.put(entry.getKey(), DDDConfigurations.mobResists.getOrFallbackToDefault(entry.getValue()));
			DebugLib.outputFormattedDebug("Minion %s copied resistances of %s", entry.getKey(), entry.getValue());
		});
		return IModIntegration.super.postInit(evt);
	}

}
