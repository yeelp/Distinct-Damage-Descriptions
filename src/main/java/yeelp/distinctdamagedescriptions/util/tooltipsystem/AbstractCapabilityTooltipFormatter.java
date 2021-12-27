package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.Translator;

/**
 * An abstract capability content formatter for DDD tooltips.
 * 
 * @author Yeelp
 *
 * @param <T> The type of capability this formatter formats.
 */
public abstract class AbstractCapabilityTooltipFormatter<C, T> extends AbstractKeyTooltipFormatter<T> {

	private Function<T, Optional<C>> capExtractor;
	private ITextComponent typeText;
	private static final Style GRAY_COLOUR = new Style().setColor(TextFormatting.GRAY);
	private static final Translator TRANSLATOR = Translations.INSTANCE.getTranslator("tooltips");

	protected AbstractCapabilityTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<T, Optional<C>> capExtractor, String typeTextKey) {
		super(keyTooltip, numberFormatter, damageFormatter);
		this.capExtractor = capExtractor;
		this.typeText = TRANSLATOR.getComponent(typeTextKey, GRAY_COLOUR);
	}

	/**
	 * Format a capability for placement in an ItemStack's tooltip
	 * 
	 * @param stack the stack whose tooltip will gain the content
	 * @param cap   the capability instance
	 * @return an Optional containing a List of Strings if any are to be added, or
	 *         an empty Optional if no Strings are to be added.
	 */
	protected abstract Optional<List<String>> formatCapabilityFor(@Nonnull T t, @Nonnull C cap);

	@Override
	public List<String> format(T t) {
		List<String> result = new LinkedList<String>();
		result.add(this.typeText.getFormattedText() + this.getKeyText());
		if(this.shouldShow() && t != null) {
			Optional<List<String>> formattedCap = this.capExtractor.apply(t).flatMap((c) -> this.formatCapabilityFor(t, c));
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

	protected static ITextComponent getComponentWithGrayColour(String key) {
		return getComponentWithStyle(key, GRAY_COLOUR);
	}

	protected static ITextComponent getComponentWithStyle(String key, Style style) {
		return TRANSLATOR.getComponent(key, style);
	}
}
