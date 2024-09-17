package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;

/**
 * Formatter for DDDDamageTypes
 * 
 * @author Yeelp
 *
 */
public enum DDDDamageFormatter implements ObjectFormatter<DDDDamageType> {
	/**
	 * Formats DDDDamageTypes with additional colour information prepended at the
	 * start of the damage name.
	 */
	COLOURED {
		@Override
		public String format(DDDDamageType t) {
			return t.getFormattedDisplayName();
		}
	},

	/**
	 * Formats DDDDamageTypes as plain text, with no colour information.
	 */
	STANDARD {
		@Override
		public String format(DDDDamageType t) {
			return t.getDisplayName();
		}
	};
}
