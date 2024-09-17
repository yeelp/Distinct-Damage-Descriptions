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

	private DDDDamageFormatter damageFormatter;

	protected AbstractTooltipFormatter(DDDDamageFormatter damageFormatter) {
		this.setDamageFormatter(damageFormatter);
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
	 * Get the current DDDDamageFormatter
	 * 
	 * @return the current DDDDamageFormatter
	 */
	protected DDDDamageFormatter getDamageFormatter() {
		return this.damageFormatter;
	}
}
