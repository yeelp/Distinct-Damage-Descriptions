package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobResistancesFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipFormatterUtilities;

/**
 * An icon aggregator for mob resistances
 * 
 * @author Yeelp
 *
 */
public class MobResistanceIconAggregator extends AbstractCapabilityIconAggregator {

	private static MobResistanceIconAggregator instance;
	private final Function<ItemStack, Optional<ResourceLocation>> resourceLocationGetter;

	private MobResistanceIconAggregator() {
		this(MobResistancesFormatter.getInstance(), TooltipFormatterUtilities::getResourceLocationFromSpawnEgg);
	}

	protected <C, T> MobResistanceIconAggregator(AbstractCapabilityTooltipFormatter<C, T> formatter, Function<ItemStack, Optional<ResourceLocation>> f) {
		super(TextFormatting.GRAY.toString().concat(formatter.getTypeText().getFormattedText()), formatter::shouldShow);
		this.resourceLocationGetter = f;
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static MobResistanceIconAggregator getInstance() {
		return instance == null ? instance = new MobResistanceIconAggregator() : instance;
	}

	@Override
	protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		return this.resourceLocationGetter.apply(stack).flatMap(TooltipFormatterUtilities::getMobResistancesIfConfigured).map((r) -> r.getResistanceMap().entrySet().stream().sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).map(Entry::getKey)).orElse(Stream.empty());
	}
}
