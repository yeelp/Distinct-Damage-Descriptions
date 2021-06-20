package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import yeelp.distinctdamagedescriptions.ModConfig;

/**
 * An abstract content formatter whose content depends on a KeyTooltip
 * @author Yeelp
 *
 */
public abstract class AbstractKeyTooltipFormatter extends AbstractTooltipFormatter {

	private KeyTooltip keyTooltip;
	
	protected AbstractKeyTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter) {
		super(numberFormatter, damageFormatter);
		this.keyTooltip = keyTooltip;
	}

	/**
	 * Should the content of this formatter be shown? This does not need to indicate that no text is to be shown, simple text indicating "Hold KEY for info" is an expected use case.
	 * This merely indicates if additional content (the actual content) should be shown
	 * @return true if the content should be shown.
	 */
	public boolean shouldShow() {
		return this.keyTooltip.checkKeyIsHeld() || ModConfig.client.neverHideInfo;
	}
	
	/**
	 * Get the text for the key that needs to be held, if holding it actually changes tooltip content. Just calls {@code KeyTooltip.getKeyText()}
	 * @return The String from the same call to the internal KeyTooltip, if the "Never Hide Info" config option is set to false, otherwise an empty String
	 * @see KeyTooltip#getKeyText()
	 */
	public String getKeyText() {
		return !ModConfig.client.neverHideInfo ? this.keyTooltip.getKeyText() : "";
	}

}
