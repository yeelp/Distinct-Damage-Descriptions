package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.util.text.ITextComponent;

/**
 * An abstract capability content formatter for DDD tooltips.
 * @author Yeelp
 *
 * @param <T> The type of capability this formatter formats.
 */
public abstract class AbstractCapabilityTooltipFormatter<C, T> extends AbstractKeyTooltipFormatter<T> {

	private Function<T, C> capExtractor;
	private ITextComponent typeText;

	protected AbstractCapabilityTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<T, C> capExtractor, ITextComponent typeText) {
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
	protected abstract Optional<List<String>> formatCapabilityFor(T t, C cap);
	
	@Override
	public List<String> format(T t) {
		List<String> result = new LinkedList<String>();
		result.add(this.typeText.getFormattedText() + this.getKeyText());
		if(this.shouldShow()) {
			Optional<List<String>> formattedCap = formatCapabilityFor(t, this.capExtractor.apply(t));
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
