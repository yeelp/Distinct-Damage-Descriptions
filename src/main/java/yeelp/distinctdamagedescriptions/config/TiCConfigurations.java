package yeelp.distinctdamagedescriptions.config;

import java.util.HashMap;
import java.util.Map;

import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
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
	public static IDDDConfiguration<DistributionBias> weaponMaterialBias;

	/**
	 * Configuration for armor material biases for mod integration.
	 */
	public static IDDDConfiguration<DistributionBias> armorMaterialBias;

	/**
	 * Configuration for item bias resistance for mod integration.
	 */
	public static IDDDConfiguration<Float> biasResistance;

	@Initializer
	public static void init() {
		Map<DDDDamageType, Float> defaultWeaponMaterialBias = new HashMap<DDDDamageType, Float>();
		defaultWeaponMaterialBias.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f);
		weaponMaterialBias = new DDDBaseConfiguration<DistributionBias>(new DistributionBias(defaultWeaponMaterialBias, 0));
		armorMaterialBias = new DDDBaseConfiguration<DistributionBias>(new DistributionBias(new HashMap<DDDDamageType, Float>(), 0));
		biasResistance = new DDDBaseConfiguration<Float>(0.0f);
	}
}
