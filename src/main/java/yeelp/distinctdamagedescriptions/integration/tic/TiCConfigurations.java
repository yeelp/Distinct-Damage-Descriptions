package yeelp.distinctdamagedescriptions.integration.tic;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDDistributionConfiguration;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDArmorDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDDistributionBiasConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDNumericEntryConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.integration.tic.dists.TiCBleedDistribution;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

/**
 * TConstruct and Conarm specific Configurations
 * 
 * @author Yeelp
 *
 */
@DDDLoader(modid = ModConsts.IntegrationIds.TCONSTRUCT_ID, name = "TiC Configurations", requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class TiCConfigurations {

	/**
	 * Configuration for weapon material biases for mod integration.
	 */
	public static IDDDConfiguration<DistributionBias> toolMaterialBias;

	/**
	 * Configuration for armor material biases for mod integration.
	 */
	public static IDDDConfiguration<IArmorDistribution> armorMaterialDist;

	/**
	 * Configuration for item bias resistance for mod integration.
	 */
	public static IDDDConfiguration<Float> toolBiasResistance;

	@Initializer
	public static void init() {
		DDDBaseMap<Float> defaultWeaponMaterialBias = new DDDBaseMap<Float>(() -> 0.0f);
		defaultWeaponMaterialBias.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f);
		toolMaterialBias = new DDDBaseConfiguration<DistributionBias>(() -> new DistributionBias(defaultWeaponMaterialBias, 0.0f));
		armorMaterialDist = new DDDDistributionConfiguration<IArmorDistribution>(() -> new ArmorDistribution());
		toolBiasResistance = new DDDBaseConfiguration<Float>(() -> 0.0f);
		try {
			DDDConfigLoader.getInstance().enqueueAll(new DDDModIDPrependingConfigReader<Float>(ModConsts.IntegrationIds.TCONSTRUCT_ID, new DDDNumericEntryConfigReader<Float>("Tinker's Compat: Tool Bias", ModConfig.compat.tinkers.toolBias, toolBiasResistance, Float::parseFloat)),
					new DDDDistributionBiasConfigReader("Tinker's Compat: Material Bias", ModConfig.compat.tinkers.matBias, toolMaterialBias),
					new DDDArmorDistributionConfigReader("Conarm Compat: Material Distribution", ModConfig.compat.conarm.armorMatDist, armorMaterialDist),
					TiCBleedDistribution.getConfigReader());
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
