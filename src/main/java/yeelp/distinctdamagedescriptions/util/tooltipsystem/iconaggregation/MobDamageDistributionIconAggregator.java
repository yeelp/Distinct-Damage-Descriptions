package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipFormatterUtilities;

/**
 * An icon aggregator for mob damage distributions
 * 
 * @author Yeelp
 *
 */
public class MobDamageDistributionIconAggregator extends DistributionIconAggregator<IDamageDistribution> {
	private static MobDamageDistributionIconAggregator instance;

	private MobDamageDistributionIconAggregator() {
		this(MobDamageDistributionFormatter.getInstance(), TooltipFormatterUtilities::getResourceLocationFromSpawnEgg);
	}

	protected MobDamageDistributionIconAggregator(AbstractCapabilityTooltipFormatter<IDamageDistribution, ItemStack> formatter, Function<ItemStack, Optional<ResourceLocation>> f) {
		super(formatter, (s) -> f.apply(s).flatMap(TooltipFormatterUtilities::getMobDamageIfConfigured));
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singletons instance
	 */
	public static MobDamageDistributionIconAggregator getInstance() {
		return instance == null ? instance = new MobDamageDistributionIconAggregator() : instance;
	}
	
	@Override
	protected boolean shouldKeepUnknown() {
		return true;
	}
}
