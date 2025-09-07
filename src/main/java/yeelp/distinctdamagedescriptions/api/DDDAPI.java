package yeelp.distinctdamagedescriptions.api;

import yeelp.distinctdamagedescriptions.api.impl.DistinctDamageDescriptionsAPIImpl;
import yeelp.distinctdamagedescriptions.init.DDDLoader;
import yeelp.distinctdamagedescriptions.init.DDDLoader.Initializer;
import yeelp.distinctdamagedescriptions.init.DDDLoaderIDs;

/**
 * API for Distinct Damage Descriptions
 * 
 * @author Yeelp
 *
 */
@DDDLoader(name = DDDLoaderIDs.API)
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
