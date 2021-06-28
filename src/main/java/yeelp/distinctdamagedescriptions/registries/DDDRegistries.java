package yeelp.distinctdamagedescriptions.registries;

import yeelp.distinctdamagedescriptions.registries.impl.DDDCreatureTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDamageTypes;
import yeelp.distinctdamagedescriptions.registries.impl.DDDDistributions;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.util.DDDJsonIO;

/**
 * All the registered info for DDD goes here. Damage Types, Creature Types, and
 * Damage Distributions.
 * 
 * @author Yeelp
 *
 */
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
	 * Initialize the registries, update the explosion distribution, and read from
	 * JSON
	 */
	public static void init() {
		damageTypes = new DDDDamageTypes();
		creatureTypes = new DDDCreatureTypes();
		distributions = new DDDDistributions();
		DDDExplosionDist.update();
		distributions.register(DDDJsonIO.init());
	}
}
