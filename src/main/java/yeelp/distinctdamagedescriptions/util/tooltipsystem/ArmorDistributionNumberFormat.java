package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.function.Predicate;

public enum ArmorDistributionNumberFormat implements ObjectFormatter<Float>, Predicate<Float> {
	PLAIN(DDDNumberFormatter.PLAIN) {
		@Override
		public boolean test(Float t) {
			return t != 0.0f;
		}
	},
	PERCENT(DDDNumberFormatter.PERCENT) {
		@Override
		public boolean test(Float t) {
			return t != 0.0f;
		}
	},
	RELATIVE(DDDNumberFormatter.RELATIVE) {
		@Override
		public boolean test(Float t) {
			return t != 1.0f;
		}
	};

	private final ObjectFormatter<Float> formatter;
	
	private ArmorDistributionNumberFormat(ObjectFormatter<Float> formatter) {
		this.formatter = formatter;
	}
	@Override
	public String format(Float t) {
		return this.formatter.format(t);
	}
}
