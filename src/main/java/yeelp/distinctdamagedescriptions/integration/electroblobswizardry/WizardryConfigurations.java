package yeelp.distinctdamagedescriptions.integration.electroblobswizardry;

import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;

/**
 * EBWizardry specific configs
 * 
 * @author Yeelp
 *
 */
@DDDLoader(modid = IntegrationIds.WIZARDRY_ID, name = "Electroblob's Wizardry Configurations", requiredLoaders = "DDD Registries")
public abstract class WizardryConfigurations {

	/**
	 * Configuration for Spell Damage Distributions.
	 */
	public static IDDDConfiguration<IDamageDistribution> spellDist;
	
	/**
	 * Configuration of minion resistance delegates.
	 */
	public static IDDDConfiguration<String> minionResistancesReference;
	
	@Initializer
	public static void init() {
		spellDist = new DDDBaseConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.FORCE.getBaseDistribution());
		minionResistancesReference = new DDDBaseConfiguration<String>(() -> "");
		try {
			DDDConfigLoader.getInstance().enqueueAll(
					new DDDModIDPrependingConfigReader<IDamageDistribution>(IntegrationIds.WIZARDRY_ID, new DDDDamageDistributionConfigReader("Electroblob's Wizardry Compat: Spell Distributions", ModConfig.compat.ebwizardry.spellDistributions, spellDist)),
					new DDDModIDPrependingConfigReader<String>(IntegrationIds.WIZARDRY_ID, new WizardryMinionConfigReader()));
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
