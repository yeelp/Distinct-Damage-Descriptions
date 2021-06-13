package yeelp.distinctdamagedescriptions.util.tooltipsystem;

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

	@Override
	public boolean shouldShow() {
		return this.keyTooltip.checkKeyIsHeld();
	}
	
	/**
	 * Get the text for the key that needs to be held. Just calls {@code KeyTooltip.getKeyText()}
	 * @return The String from the same call to the internal KeyTooltip.
	 * @see KeyTooltip#getKeyText()
	 */
	public String getKeyText() {
		return this.keyTooltip.getKeyText();
	}

}
