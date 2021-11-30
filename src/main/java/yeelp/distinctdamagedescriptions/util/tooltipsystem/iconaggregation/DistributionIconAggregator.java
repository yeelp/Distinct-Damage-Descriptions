package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;

/**
 * A more concrete, yet still abstract skeleton of an icon aggregator for
 * IDistributions
 * 
 * @author Yeelp
 *
 * @param <T> The type of IDistribution this DistributionIconAggregator
 *            aggregates icons for.
 */
public abstract class DistributionIconAggregator<T extends IDistribution> extends AbstractCapabilityIconAggregator {

	private final Function<ItemStack, Optional<T>> capExtractor;

	protected DistributionIconAggregator(AbstractCapabilityTooltipFormatter<T, ItemStack> formatter, Function<ItemStack, Optional<T>> capExtractor) {
		//Minecraft adds a gray colour formatting code at the beginning of every tooltip string (except the first one), so the formatting codes are duplicated
		super(TextFormatting.GRAY.toString()+formatter.getTypeText().getFormattedText(), formatter::shouldShow);
		this.capExtractor = capExtractor;
	}

	@Override
	protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		return this.capExtractor.apply(stack).map((c) -> c.getCategories().stream().sorted()).orElse(Stream.empty());
	}
}
