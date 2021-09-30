package yeelp.distinctdamagedescriptions.api;

import yeelp.distinctdamagedescriptions.api.impl.DistinctDamageDescriptionsAPIImpl;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;

/**
 * API for Distinct Damage Descriptions
 * 
 * @author Yeelp
 *
 */
@DDDLoader(name = "DDD API")
public abstract class DDDAPI {
	public static IDistinctDamageDescriptionsAccessor accessor;
	public static IDistinctDamageDescriptionsMutator mutator;

	/**
	 * Initialize the API. Only needed to be called once. DistinctDamageDescriptions
	 * does this during startup in preinit.
	 */
	@Initializer(shouldTime = false)
	public static void init() {
		DistinctDamageDescriptionsAPIImpl.values();
	}
}
