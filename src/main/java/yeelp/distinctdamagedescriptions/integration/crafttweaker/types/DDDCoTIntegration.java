package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Collections;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class DDDCoTIntegration implements IModIntegration {

	@Override
	public Iterable<Handler> getHandlers() {
		return Collections.emptyList();
	}
	
	

	@Override
	public boolean preInit(FMLPreInitializationEvent evt) {
		CoTDDDDamageTypeBuilder.registerTypes();
		return IModIntegration.super.preInit(evt);
	}



	@Override
	public boolean init(FMLInitializationEvent evt) {
		CoTDDDDistributionBuilder.registerDists();
		return IModIntegration.super.init(evt);
	}

	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.CONTENTTWEAKER_TITLE;
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.CONTENTTWEAKER_ID;
	}

	

}
