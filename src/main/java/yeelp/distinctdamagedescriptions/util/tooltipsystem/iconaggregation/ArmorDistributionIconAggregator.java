package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionFormatter;

/**
 * An icon aggregator for IArmorDistributions
 * 
 * @author Yeelp
 *
 */
public class ArmorDistributionIconAggregator extends DistributionIconAggregator<IArmorDistribution> {

	private static ArmorDistributionIconAggregator instance;

	protected ArmorDistributionIconAggregator() {
		super(ArmorDistributionFormatter.getInstance(), DDDAPI.accessor::getArmorResistances);
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static ArmorDistributionIconAggregator getInstance() {
		return instance == null ? instance = new ArmorDistributionIconAggregator() : instance;
	}
}
