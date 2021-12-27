package yeelp.distinctdamagedescriptions.util.tooltipsystem;

/**
 * Basic implementation of {@link TooltipFormatter}
 * 
 * @author Yeelp
 * 
 * @see TooltipFormatter
 * @param <T> The kind of objects formatted
 */
public abstract class AbstractTooltipFormatter<T> implements TooltipFormatter<T> {

	private DDDNumberFormatter numberFormatter;
	private DDDDamageFormatter damageFormatter;

	protected AbstractTooltipFormatter(DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter) {
		this.setNumberFormatter(numberFormatter);
		this.setDamageFormatter(damageFormatter);
	}

	@Override
	public void setNumberFormatter(DDDNumberFormatter f) {
		if(this.supportsNumberFormat(f)) {
			this.numberFormatter = f;
		}
		else {
			throw new IllegalArgumentException(f + " isn't supported by this formatter!");
		}
	}

	@Override
	public void setDamageFormatter(DDDDamageFormatter f) {
		if(this.supportsDamageFormat(f)) {
			this.damageFormatter = f;
		}
		else {
			throw new IllegalArgumentException(f + " isn't supported by this formatter!");
		}
	}

	/**
	 * Get the current DDDNumberFormatter
	 * 
	 * @return the current DDDNumberFormatter
	 */
	protected DDDNumberFormatter getNumberFormatter() {
		return this.numberFormatter;
	}

	/**
	 * Get the current DDDDamageFormatter
	 * 
	 * @return the current DDDDamageFormatter
	 */
	protected DDDDamageFormatter getDamageFormatter() {
		return this.damageFormatter;
	}
}
