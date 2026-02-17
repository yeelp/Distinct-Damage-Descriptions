package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

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
	private static final Style WHITE_COLOUR = new Style().setColor(TextFormatting.WHITE);
	private static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(TooltipConsts.TOOLTIPS_ROOT);

	private static final ITextComponent NO_TOOLTIP = getComponentWithGrayColour(TooltipConsts.NO_TOOLTIP);
	protected static final ITextComponent NONE_TEXT = getComponentWithWhiteColour(TooltipConsts.NO_RESISTS);
	
	protected AbstractCapabilityTooltipFormatter(KeyTooltip keyTooltip, DDDDamageFormatter damageFormatter, Function<T, Optional<C>> capExtractor, String typeTextKey) {
		super(keyTooltip, damageFormatter);
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
			Optional<List<String>> formatCap = this.capExtractor.apply(t).flatMap((c) -> this.formatCapabilityFor(t, c));
			if(formatCap.isPresent()) {
				List<String> l = formatCap.get();
				if(ModConfig.client.useIcons) {
					l.stream().map((s) -> new StringBuilder(" ").append(s.replaceAll("  ", " ")).toString()).forEach(result::add);
				}
				else {
					result.addAll(l);
				}				
			}
			else {
				result.add(NO_TOOLTIP.getFormattedText());
			}
		}
		return result;
	}

	public ITextComponent getTypeText() {
		return this.typeText;
	}

	protected static ITextComponent getComponentWithGrayColour(String key) {
		return getComponentWithStyle(key, GRAY_COLOUR);
	}
	
	protected static ITextComponent getComponentWithWhiteColour(String key) {
		return getComponentWithStyle(key, WHITE_COLOUR);
	}

	protected static ITextComponent getComponentWithStyle(String key, Style style) {
		return TRANSLATOR.getComponent(key, style);
	}
}
