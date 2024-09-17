package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ShieldDistributionFormatter;

/**
 * An icon aggregator for ShieldDistributions
 * 
 * @author Yeelp
 *
 */
public class ShieldDistributionIconAggregator extends DistributionIconAggregator<ShieldDistribution> {

	private static ShieldDistributionIconAggregator instance;

	private ShieldDistributionIconAggregator() {
		super(ShieldDistributionFormatter.getInstance(), DDDAPI.accessor::getShieldDistribution);
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static ShieldDistributionIconAggregator getInstance() {
		return instance == null ? instance = new ShieldDistributionIconAggregator() : instance;
	}
	
	@Override
	protected boolean shouldKeepUnknown() {
		return false;
	}
}
