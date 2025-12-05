package yeelp.distinctdamagedescriptions.config;

import java.util.HashSet;
import java.util.Map;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.readers.DDDArmorDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDBasicConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDDamageDistributionConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDMobResistancesConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDProjectileConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDShieldClassesConfigReader;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.util.lib.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * A collection of all the configuration info DDD uses.
 * 
 * @author Yeelp
 *
 */
@DDDLoader(name = DDDLoaderIDs.CONFIGURATIONS, requiredLoaders = DDDLoaderIDs.REGISTRIES)
public abstract class DDDConfigurations {

	/**
	 * Configuration for items
	 */
	public static IDDDConfiguration<IDamageDistribution> items;

	/**
	 * Configuration for armors
	 */
	public static IDDDConfiguration<IArmorDistribution> armors;

	/**
	 * Configurations for shields
	 */
	public static IDDDConfiguration<ShieldDistribution> shields;

	/**
	 * Configuration for mob damage
	 */
	public static IDDDConfiguration<IDamageDistribution> mobDamage;

	/**
	 * Configurations for mob resistances
	 */
	public static IDDDConfiguration<MobResistanceCategories> mobResists;

	/**
	 * Configuration for projectiles, which includes a method to get linked items
	 */
	public static IDDDProjectileConfiguration projectiles;

	/**
	 * Initializes the configurations and reads from the config.
	 */
	@Initializer
	public static void init() {
		items = new DDDDistributionConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution()).wrapInMetadataAcceptingConfiguration();
		armors = new DDDDistributionConfiguration<IArmorDistribution>(() -> new ArmorDistribution()).wrapInMetadataAcceptingConfiguration();
		shields = new DDDDistributionConfiguration<ShieldDistribution>(() -> new ShieldDistribution()).wrapInMetadataAcceptingConfiguration();
		mobDamage = new DDDDistributionConfiguration<IDamageDistribution>(() -> DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution());
		mobResists = new DDDBaseConfiguration<MobResistanceCategories>(() -> new MobResistanceCategories(new NonNullMap<DDDDamageType, Float>(() -> 0.0f), new HashSet<DDDDamageType>(), 0.0f, 0.0f));
		projectiles = new DDDProjectileConfiguration(() -> DDDBuiltInDamageType.PIERCING.getBaseDistribution());
		String[] resistsConfig = new String[ModConfig.resist.mobBaseResist.length + 1];
		System.arraycopy(ModConfig.resist.mobBaseResist, 0, resistsConfig, 1, ModConfig.resist.mobBaseResist.length);
		resistsConfig[0] = "player;" + ModConfig.resist.playerResists;
		try {
			DDDConfigLoader.getInstance().enqueueAll(new DDDDamageDistributionConfigReader("Item Distributions", ModConfig.dmg.itemBaseDamage, items),
					new DDDArmorDistributionConfigReader("Armor Distributions", ModConfig.resist.armorResist, armors),
					new DDDBasicConfigReader<ShieldDistribution>("Shield Distributions", ModConfig.resist.shieldResist, shields, ShieldDistribution.class.getConstructor(Map.class), 0.0f),
					new DDDDamageDistributionConfigReader("Mob Damage Distributions", ModConfig.dmg.mobBaseDmg, mobDamage),
					new DDDMobResistancesConfigReader(resistsConfig),
					new DDDProjectileConfigReader(),
					new DDDShieldClassesConfigReader());
		}
		catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
