package yeelp.distinctdamagedescriptions.integration.fermiumbooter;

import com.google.common.collect.ImmutableList;

import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.OptionalMixinKernel;

public final class FermiumBooterIntegration implements IModIntegration {

	private static FermiumBooterIntegration instance;
	
	private FermiumBooterIntegration() {
		//nothing
	}
	
	@Override
	public String getModTitle() {
		return IntegrationTitles.FERMIUM_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.FERMIUM_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}
	
	public static FermiumBooterIntegration getInstance() {
		return instance == null ? instance = new FermiumBooterIntegration() : instance;
	}
	
	public static boolean hasFermiumBooter() {
		return OptionalMixinKernel.wasFermiumBooterPresent();
	}

}
