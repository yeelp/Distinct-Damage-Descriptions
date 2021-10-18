package yeelp.distinctdamagedescriptions.integration.client;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.AbstractCapabilityIconAggregator;

public abstract class AbstractModCompatIconAggregator<T extends IDistributionRequiresUpdate> extends AbstractCapabilityIconAggregator implements IModCompatIconAggregator {

	private final Function<ItemStack, T> capExtractor;
	
	protected <F extends AbstractModCompatTooltipFormatter> AbstractModCompatIconAggregator(F formatter, Capability<? extends T> cap) {
		super(TextFormatting.GRAY.toString().concat(formatter.getTypeText().getFormattedText()), formatter::shouldShow);
		this.capExtractor = (stack) -> stack.getCapability(cap, null);
	}

	@Override
	protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		T dist = this.capExtractor.apply(stack);
		if(dist != null) {
			return dist.getUpdatedDistribution(stack).orElse(this.getDefaultDistribution(stack)).keySet().stream().sorted();
		}
		return Stream.empty();
	}
	
	protected abstract Map<DDDDamageType, Float> getDefaultDistribution(ItemStack stack);

}
