package yeelp.distinctdamagedescriptions.integration.tic;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.*;
import yeelp.distinctdamagedescriptions.config.readers.DDDArmorDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDDistributionBiasConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDNumericEntryConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
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
		DDDBaseMap<Float> defaultWeaponMaterialBias = new DDDBaseMap<>(() -> 0.0f);
		defaultWeaponMaterialBias.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f);
		toolMaterialBias = new DDDBaseConfiguration<>(() -> new DistributionBias(defaultWeaponMaterialBias, 0.0f));
		armorMaterialDist = new DDDDistributionConfiguration<>(ArmorDistribution::new);
		toolBiasResistance = new DDDBaseConfiguration<>(() -> 0.0f);
		try {
			DDDConfigLoader.getInstance().enqueueAll(new DDDModIDPrependingConfigReader<>(ModConsts.IntegrationIds.TCONSTRUCT_ID, new DDDNumericEntryConfigReader<>("Tinker's Compat: Tool Bias", ModConfig.compat.tinkers.toolBias, toolBiasResistance, Float::parseFloat)),
					new DDDDistributionBiasConfigReader("Tinker's Compat: Material Bias", ModConfig.compat.tinkers.matBias, toolMaterialBias),
					new DDDArmorDistributionConfigReader("Conarm Compat: Material Distribution", ModConfig.compat.conarm.armorMatDist, armorMaterialDist),
					TiCBleedDistribution.getConfigReader());
		}
		catch(NoSuchMethodException | SecurityException e) {
			DistinctDamageDescriptions.logStackTrace(e, DistinctDamageDescriptions::fatal);
			throw new RuntimeException(e);
		}
	}
}
