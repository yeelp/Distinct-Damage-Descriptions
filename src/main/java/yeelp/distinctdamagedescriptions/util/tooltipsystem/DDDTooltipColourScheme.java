package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import net.minecraft.util.text.TextFormatting;

public enum DDDTooltipColourScheme {

	RED_GREEN(TextFormatting.RED, TextFormatting.GREEN),
	GRAYSCALE(TextFormatting.DARK_GRAY, TextFormatting.WHITE),
	WHITE(TextFormatting.WHITE);
	
	private final TextFormatting negative, positive;
	private DDDTooltipColourScheme(TextFormatting negative, TextFormatting positive) {
		this.negative = negative;
		this.positive = positive;
	}
	
	private DDDTooltipColourScheme(TextFormatting colour) {
		this(colour, colour);
	}
	
	public TextFormatting getFormattingBasedOnValue(float value, float threshold) {
		return value < threshold ? this.negative : this.positive;
	}
}
