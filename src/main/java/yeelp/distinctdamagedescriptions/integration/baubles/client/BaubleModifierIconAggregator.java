package yeelp.distinctdamagedescriptions.integration.baubles.client;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.baubles.BaublesConfigurations;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.AbstractCapabilityIconAggregator;

public final class BaubleModifierIconAggregator extends AbstractCapabilityIconAggregator {

	private static BaubleModifierIconAggregator instance;
	private BaubleModifierIconAggregator() {
		super(TextFormatting.GRAY.toString() + BaubleModifierFormatter.START.getFormattedText(), () -> KeyTooltip.CTRL.checkKeyIsHeld() || ModConfig.client.neverHideInfo);
	}

	@Override
	protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
		return YResources.getRegistryString(stack).filter(BaublesConfigurations.baubleModifiers::configured)
				.map(BaublesConfigurations.baubleModifiers::get)
				.map((m) -> {
					Builder<DDDDamageType> builder = Stream.builder();
					for(BaubleModifierType type : BaubleModifierType.values()) {
						Comparator<Entry<DDDDamageType, Float>> comparator = BaubleModifierFormatter.getComparator(type);
						if(m.containsKey(type)) {
							m.get(type).getModifications().entrySet().stream().sorted(comparator).map(Entry::getKey).forEach(builder::add);
						}
					}
					return builder.build();
				}).orElse(Stream.empty());
	}
	
	public static BaubleModifierIconAggregator getInstance() {
		return instance == null ? new BaubleModifierIconAggregator() : instance;
	}

}
