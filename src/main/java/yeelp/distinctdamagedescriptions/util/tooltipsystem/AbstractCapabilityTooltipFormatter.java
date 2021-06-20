package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/**
 * An abstract capability content formatter for DDD tooltips.
 * @author Yeelp
 *
 * @param <T> The type of capability this formatter formats.
 */
public abstract class AbstractCapabilityTooltipFormatter<T> extends AbstractKeyTooltipFormatter {

	private Function<ItemStack, T> capExtractor;
	private ITextComponent typeText;

	protected AbstractCapabilityTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<ItemStack, T> capExtractor, ITextComponent typeText) {
		super(keyTooltip, numberFormatter, damageFormatter);
		this.capExtractor = capExtractor;
		this.typeText = typeText;
	}
	
	/**
	 * Format a capability for placement in an ItemStack's tooltip
	 * @param stack the stack whose tooltip will gain the content
	 * @param cap the capability instance
	 * @return an Optional containing a List of Strings if any are to be added, or an empty Optional if no Strings are to be added.
	 */
	protected abstract Optional<List<String>> formatCapabilityFor(ItemStack stack, T cap);
	
	@Override
	public List<String> format(ItemStack stack) {
		List<String> result = new LinkedList<String>();
		result.add(typeText.getFormattedText() + super.getKeyText());
		if(this.shouldShow()) {
			Optional<List<String>> formattedCap = formatCapabilityFor(stack, capExtractor.apply(stack));
			formattedCap.ifPresent((l) -> {
				if(this.getDamageFormatter() == DDDDamageFormatter.ICON) {
					l.stream().map((s) -> new StringBuilder(" ").append(s.replaceAll("  ", " ")).toString()).forEach(result::add);
				}
				else {
					result.addAll(l);
				}
			});
		}
		return result;
	}
	
	public ITextComponent getTypeText() {
		return this.typeText;
	}
}
