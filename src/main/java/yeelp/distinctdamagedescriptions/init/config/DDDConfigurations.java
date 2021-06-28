package yeelp.distinctdamagedescriptions.init.config;

import java.util.HashSet;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.DDDConfigReader;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * A collection of all the configuration info DDD uses.
 * 
 * @author Yeelp
 *
 */
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
	public static void init() {
		items = new DDDBaseConfiguration<IDamageDistribution>(DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution());
		armors = new DDDBaseConfiguration<IArmorDistribution>(new ArmorDistribution());
		shields = new DDDBaseConfiguration<ShieldDistribution>(new ShieldDistribution());
		mobDamage = new DDDBaseConfiguration<IDamageDistribution>(DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution());
		mobResists = new DDDBaseConfiguration<MobResistanceCategories>(new MobResistanceCategories(new NonNullMap<DDDDamageType, Float>(0.0f), new HashSet<DDDDamageType>(), 0.0f, 0.0f));
		projectiles = new DDDProjectileConfiguration(DDDBuiltInDamageType.PIERCING.getBaseDistribution());
		DDDConfigReader.readConfig();
	}
}
