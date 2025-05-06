package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.function.Predicate;

import yeelp.distinctdamagedescriptions.config.ModConfig;

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
			switch(ModConfig.resist.armorParseRule) {
				case IMPLIED:
					return t != ModConfig.resist.impliedArmorEffectiveness;
				default:
					return t != 0.0f;
			}
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
