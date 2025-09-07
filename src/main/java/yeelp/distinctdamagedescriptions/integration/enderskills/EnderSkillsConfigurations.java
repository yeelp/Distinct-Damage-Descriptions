package yeelp.distinctdamagedescriptions.integration.enderskills;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDMultiEntryConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.integration.enderskills.dist.EnderSkillsPredefinedDistribution;

@DDDLoader(modid = ModConsts.IntegrationIds.ENDER_SKILLS_ID, name = "Ender Skills Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class EnderSkillsConfigurations {

	/**
	 * Configuration for skill distributions.
	 */
	public static IDDDConfiguration<IDamageDistribution> skillDist;
	
	/**
	 * Configuration for skill damage over time effects.
	 */
	public static IDDDConfiguration<IDamageDistribution> skillDotDist;
	
	@Initializer
	public static void init() {
		skillDist = new DDDBaseConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.FORCE.getBaseDistribution());
		skillDotDist = new DDDBaseConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.FORCE.getBaseDistribution());
		try {
			DDDConfigLoader.getInstance().enqueueAll(
					wrapModIDReader(new DDDDamageDistributionConfigReader("Ender Skills Compat: Skill Distributions", ModConfig.compat.enderskills.skillDistribution, skillDist)), 
					wrapModIDReader(new DDDDamageDistributionConfigReader("Ender Skills Compat: Skill DOT Distributions", ModConfig.compat.enderskills.skillDotDistribution, skillDotDist)));
			DDDConfigLoader.getInstance().enqueueAll(EnderSkillsPredefinedDistribution.getReaders());
		}
		catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static final <T> DDDConfigReader wrapModIDReader(DDDMultiEntryConfigReader<T> internal) {
		return new DDDModIDPrependingConfigReader<T>(ModConsts.IntegrationIds.ENDER_SKILLS_ID, internal);
	}
}
