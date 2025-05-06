package yeelp.distinctdamagedescriptions.integration.electroblobswizardry;

import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.readers.AbstractWizardryConfigReader.BasicWizardryConfigReader;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.readers.WizardryMinionConfigReader;

/**
 * EBWizardry specific configs
 * 
 * @author Yeelp
 *
 */
@DDDLoader(modid = IntegrationIds.WIZARDRY_ID, name = "Electroblob's Wizardry Configurations", requiredLoaders = "DDD Registries")
public abstract class WizardryConfigurations {
	
	/**
	 * Configuration of minion capability delegates.
	 */
	public static IDDDConfiguration<String> minionCapabilityReference;
	
	/**
	 * Configuration for Spells based off damage type.
	 */
	public static IDDDConfiguration<IDamageDistribution> spellTypeDist;
	
	/**
	 * Configuration for throwables and their projectile distributions.
	 */
	public static IDDDConfiguration<String> linkedThrowables;
	
	/**
	 * Configuration for what spells deal what type of damage from EBWizardry
	 */
	public static IDDDConfiguration<String> spellTypeDamage;
	
	@Initializer
	public static void init() {
		spellTypeDist = new DDDBaseConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.FORCE.getBaseDistribution());
		spellTypeDamage = new DDDBaseConfiguration<String>(() -> "");
		minionCapabilityReference = new DDDBaseConfiguration<String>(() -> "");
		linkedThrowables = new DDDBaseConfiguration<String>(() -> "");
		try {
			DDDConfigLoader.getInstance().enqueueAll(
					new DDDDamageDistributionConfigReader("Electroblob's Wizardry Compat: Spell Type Distributions", ModConfig.compat.ebwizardry.spellDamageTypeDistributions, spellTypeDist),
					new WizardryMinionConfigReader().wrapInModIDConfigReader(),
					new BasicWizardryConfigReader("Electroblob's Wizardry: Linked Throwables", ModConfig.compat.ebwizardry.linkedThrowables, linkedThrowables).wrapInModIDConfigReader(),
					new BasicWizardryConfigReader("Electroblob's Wizardry Compat: Spell Type Damage", ModConfig.compat.ebwizardry.spellDamageType, spellTypeDamage).wrapInModIDConfigReader());
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
