package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.api.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.registries.impl.DDDCreatureTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDistributions;
import yeelp.distinctdamagedescriptions.registries.impl.DDDModifierRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.DDDPotionsRegistry;
import yeelp.distinctdamagedescriptions.registries.impl.DDDTrackers;
import yeelp.distinctdamagedescriptions.util.json.DDDJsonIO;

/**
 * All the registered info for DDD goes here. Damage Types, Creature Types,
 * Damage Distributions and trackers.
 * 
 * @author Yeelp
 *
 */
@DDDLoader(name = DDDLoaderIDs.REGISTRIES)
public abstract class DDDRegistries {
	/**
	 * The registry for creature types. Still initialized even if creature types
	 * aren't used.
	 */
	public static IDDDCreatureTypeRegistry creatureTypes;

	/**
	 * The registry for damage types.
	 */
	public static IDDDDamageTypeRegistry damageTypes;

	/**
	 * The registry for custom distributions.
	 */
	public static IDDDDistributionRegistry distributions;

	/**
	 * Registry for tracking entities.
	 */
	public static IDDDTrackersRegistry trackers;

	/**
	 * Registry for potions.
	 */
	public static IDDDPotionsRegistry potions;
	
	/**
	 * Registries for all capability modifiers.
	 */
	public static IDDDModifierRegistries modifiers;

	/**
	 * Initialize the registries, update the explosion distribution, and read from
	 * JSON
	 */
	@Initializer
	public static void init() {
		damageTypes = new DDDDamageTypes();
		creatureTypes = new DDDCreatureTypes();
		distributions = new DDDDistributions();
		trackers = new DDDTrackers();
		DDDExplosionDist.update();
		potions = new DDDPotionsRegistry();
		modifiers = DDDModifierRegistries.INSTANCE;
		if(ModConfig.core.useCustomDamageTypesFromJSON) {
			distributions.register(DDDJsonIO.loadDamageTypes());
		}
		if(ModConfig.core.useCreatureTypesFromJSON) {
			DDDJsonIO.loadCreatureTypes();
		}
	}
}
