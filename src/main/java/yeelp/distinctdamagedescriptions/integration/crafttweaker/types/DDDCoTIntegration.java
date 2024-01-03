package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Collections;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class DDDCoTIntegration implements IModIntegration {

	@Override
	public String getModID() {
		return ModConsts.CONTENTTWEAKER_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return Collections.emptyList();
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		CoTDDDDistributionBuilder.registerDists();
		return IModIntegration.super.init(evt);
	}

}
