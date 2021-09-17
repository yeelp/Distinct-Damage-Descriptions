package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobResistancesFormatter;

/**
 * An icon aggregator for mob resistances
 * @author Yeelp
 *
 */
public class MobResistanceIconAggregator extends AbstractCapabilityIconAggregator {

	private static MobResistanceIconAggregator instance;
	
	private MobResistanceIconAggregator() {
		super(TextFormatting.GRAY.toString()+MobResistancesFormatter.getInstance().getTypeText().getFormattedText(), MobResistancesFormatter.getInstance()::shouldShow);
	}
	
	/**
	 * Get the singleton instance
	 * @return the singleton instance
	 */
	public static MobResistanceIconAggregator getInstance() {
		return instance == null ? instance = new MobResistanceIconAggregator() : instance;
	}

	@Override
	protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		String key = Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(stack)).map(ResourceLocation::toString).orElse("");
		return DDDConfigurations.mobResists.configured(key) ? DDDConfigurations.mobResists.get(key).getResistanceMap().entrySet().stream().sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).map(Entry::getKey) : Stream.empty();
	}
}
