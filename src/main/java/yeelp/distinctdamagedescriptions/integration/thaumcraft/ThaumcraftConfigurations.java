package yeelp.distinctdamagedescriptions.integration.thaumcraft;

import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDDistributionConfiguration;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.dist.ThaumcraftPredefinedDistribution;

@DDDLoader(modid = IntegrationIds.THAUMCRAFT_ID, name = "Thaumcraft Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class ThaumcraftConfigurations {
	
	/**
	 * Damage distrbution for aspects.
	 */
	public static IDDDConfiguration<IDamageDistribution> aspectDists;
	
	@Initializer
	public static void init() {
		aspectDists = new DDDDistributionConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.FORCE.getBaseDistribution());
		try {
			DDDConfigLoader.getInstance().enqueue(new DDDDamageDistributionConfigReader("Thaumcraft Aspects Distributions", ModConfig.compat.thaumcraft.aspectDistributions, aspectDists));
			DDDConfigLoader.getInstance().enqueueAll(ThaumcraftPredefinedDistribution.getReaders());
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
