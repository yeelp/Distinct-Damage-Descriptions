package yeelp.distinctdamagedescriptions.integration.baubles;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;

@DDDLoader(modid = ModConsts.IntegrationIds.BAUBLES_ID, name = "Baubles Configurations", requiredLoaders = "DDD Registries")
public abstract class BaublesConfigurations {

	public static DDDBaublesConfiguration baubleModifiers;
	
	@Initializer
	public static void init() {
		baubleModifiers = new DDDBaublesConfiguration();
		DDDConfigLoader.getInstance().enqueue(new BaublesConfigReader());
	}
}
