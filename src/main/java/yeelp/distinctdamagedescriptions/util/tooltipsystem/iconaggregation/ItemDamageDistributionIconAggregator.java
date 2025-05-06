package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;

/**
 * An icon aggregator for item's damage distributions
 * 
 * @author Yeelp
 *
 */
public class ItemDamageDistributionIconAggregator extends DistributionIconAggregator<IDamageDistribution> {

	private static ItemDamageDistributionIconAggregator instance;

	private ItemDamageDistributionIconAggregator() {
		super(ItemDistributionFormatter.getInstance(), DDDAPI.accessor::getDamageDistribution);
	}
	
	protected ItemDamageDistributionIconAggregator(AbstractCapabilityTooltipFormatter<IDamageDistribution, ItemStack> formatter, Function<ItemStack, Optional<IDamageDistribution>> capExtractor) {
		super(formatter, capExtractor);
	}

	/**
	 * Get the singleton instance of this aggregator, making a new one if it doesn't
	 * exist
	 * 
	 * @return the singleton instance.
	 */
	public static ItemDamageDistributionIconAggregator getInstance() {
		return instance == null ? instance = new ItemDamageDistributionIconAggregator() : instance;
	}
	
	@Override
	protected boolean shouldKeepUnknown() {
		return true;
	}
}
