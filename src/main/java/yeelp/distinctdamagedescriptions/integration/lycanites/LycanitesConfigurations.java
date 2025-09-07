package yeelp.distinctdamagedescriptions.integration.lycanites;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.integration.lycanites.dists.LycanitesPredefinedDistribution;

@DDDLoader(modid = ModConsts.IntegrationIds.LYCANITES_ID, name = "Lycanites Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class LycanitesConfigurations {

	/**
	 * Collection of which entity shoots what projectile
	 */
	public static IDDDConfiguration<String> creatureProjectiles;

	@Initializer
	public static void init() {
		creatureProjectiles = new DDDBaseConfiguration<String>(() -> "");
		DDDConfigLoader.getInstance().enqueue(new LycaniteConfigReader());
		DDDConfigLoader.getInstance().enqueueAll(LycanitesPredefinedDistribution.getReaders());
	}
}
