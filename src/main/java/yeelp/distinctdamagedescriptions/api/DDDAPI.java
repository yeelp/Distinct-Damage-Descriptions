package yeelp.distinctdamagedescriptions.api;

import yeelp.distinctdamagedescriptions.api.impl.DistinctDamageDescriptionsAPIImpl;

/**
 * API for Distinct Damage Descriptions
 * @author Yeelp
 *
 */
public abstract class DDDAPI
{
	public static IDistinctDamageDescriptionsAccessor accessor;
	public static IDistinctDamageDescriptionsMutator mutator;
	
	public static void init()
	{
		DistinctDamageDescriptionsAPIImpl.values();
	}
}
