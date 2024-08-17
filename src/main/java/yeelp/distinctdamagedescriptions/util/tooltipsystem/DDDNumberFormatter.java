package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.text.DecimalFormat;

/**
 * Formatter for numbers
 * 
 * @author Yeelp
 *
 */
public enum DDDNumberFormatter implements ObjectFormatter<Float> {
	/**
	 * Formats regular numbers to two decimal places.
	 */
	PLAIN(new DecimalFormat("##.##")),

	/**
	 * Formats decimals as percents to two decimal places.
	 */
	PERCENT(new DecimalFormat("##.##%")),
	
	/**
	 * Formats decimals as relative percents to 100%, to two decimal places.
	 */
	RELATIVE(new DecimalFormat("##.##%")) {
		@Override
		public String format(Float t) {
			return super.format(t - 1.0f);
		}
	};

	private DecimalFormat formatter;

	private DDDNumberFormatter(DecimalFormat decimalFormat) {
		this.formatter = decimalFormat;
	}

	@Override
	public String format(Float t) {
		return String.format("%s%s", t < 0 ? "" : "+", this.formatter.format(t)).substring(1);
	}
}
