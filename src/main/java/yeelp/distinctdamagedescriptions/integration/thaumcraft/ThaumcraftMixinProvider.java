package yeelp.distinctdamagedescriptions.integration.thaumcraft;

import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.util.IOptionalMixinProvider;

public final class ThaumcraftMixinProvider implements IOptionalMixinProvider {

	@Override
	public String getModID() {
		return IntegrationIds.THAUMCRAFT_ID;
	}

	@Override
	public boolean enabled() {
		return ModConfig.compat.thaumcraft.enabled;
	}

}
