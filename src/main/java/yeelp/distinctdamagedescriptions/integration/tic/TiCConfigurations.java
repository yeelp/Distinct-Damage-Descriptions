package yeelp.distinctdamagedescriptions.integration.tic;

import java.util.Map;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.DDDBaseConfiguration;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDDistributionConfiguration;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDBasicConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDDistributionBiasConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.DistributionBias;

/**
 * TConstruct and Conarm specific Configurations
 * 
 * @author Yeelp
 *
 */
@DDDLoader(modid = ModConsts.TCONSTRUCT_ID, name = "TiC Configurations", requiredLoaders = "DDD Registries")
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
			DDDConfigLoader.getInstance().enqueueAll(new DDDModIDPrependingConfigReader<Float>(ModConsts.TCONSTRUCT_ID, "Tinker's Compat: Tool Bias", ModConfig.compat.tinkers.toolBias, toolBiasResistance, Float::parseFloat), new DDDDistributionBiasConfigReader("Tinker's Compat: Material Bias", ModConfig.compat.tinkers.matBias, toolMaterialBias), new DDDBasicConfigReader<IArmorDistribution>("Conarm Compat: Material Distribution", ModConfig.compat.conarm.armorMatDist, armorMaterialDist, ArmorDistribution.class.getConstructor(Map.class), 0.0f));
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
