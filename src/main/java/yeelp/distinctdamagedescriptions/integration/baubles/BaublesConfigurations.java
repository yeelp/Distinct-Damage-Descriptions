package yeelp.distinctdamagedescriptions.integration.baubles;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;

@DDDLoader(modid = ModConsts.IntegrationIds.BAUBLES_ID, name = "Baubles Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class BaublesConfigurations {

	public static DDDBaublesConfiguration baubleModifiers;
	
	@Initializer
	public static void init() {
		baubleModifiers = new DDDBaublesConfiguration();
		DDDConfigLoader.getInstance().enqueue(new BaublesConfigReader());
	}
}
