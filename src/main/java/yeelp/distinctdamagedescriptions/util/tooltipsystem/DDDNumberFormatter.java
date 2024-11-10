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
	PLAIN(new DecimalFormat("##.##")) {
		@Override
		public String format(Float t) {
			return this.formatSignless(t).toString();
		}
	},

	/**
	 * Formats decimals as percents to two decimal places.
	 */
	PERCENT(new DecimalFormat("##.##%")) {
		@Override
		public String format(Float t) {
			return this.formatSignless(t).toString();
		}
	},
	
	/**
	 * Formats decimals as relative percents to 100%, to two decimal places.
	 */
	RELATIVE(new DecimalFormat("##.##%")) {
		@Override
		public String format(Float t) {
			return new StringBuilder(t < 1 ? "-" : "+").append(this.formatSignless(t - 1.0f)).toString();
		}
	},
	
	RELATIVE_TO_ZERO(new DecimalFormat("##.##%")) {
		@Override
		public String format(Float t) {
			return new StringBuilder(t < 0 ? "-" : "+").append(this.formatSignless(t)).toString();
		}
	};

	private final DecimalFormat formatter;

	private DDDNumberFormatter(DecimalFormat decimalFormat) {
		this.formatter = decimalFormat;
	}
	
	protected final StringBuilder formatSignless(Float t) {
		StringBuilder builder = new StringBuilder();
		builder.append(this.formatter.format(t));
		if(t < 0) {
			builder.deleteCharAt(0);			
		}
		return builder;
	}
}
