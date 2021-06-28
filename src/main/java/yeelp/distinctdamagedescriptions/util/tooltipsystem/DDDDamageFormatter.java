package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;

/**
 * Formatter for DDDDamageTypes
 * @author Yeelp
 *
 */
public enum DDDDamageFormatter implements ObjectFormatter<DDDDamageType> {
	/**
	 * "Formats" DDDDamageTypes as icons. In other words, this always returns an empty String.
	 * If this {@link ObjectFormatter} is being used, client code should make sure that icons are supported for the type and drawn
	 * elsewhere, or client code should call another {@code DDDDamageFormatter} and use its {@code format} method as backup.
	 */
	ICON{
		@Override
		public String format(DDDDamageType t) {
			return "";
		}		
	},
	
	/**
	 * Formats DDDDamageTypes with additional colour information prepended at the start of the damage name.
	 */
	COLOURED{
		@Override
		public String format(DDDDamageType t) {
			return t.getFormattedDisplayName();
		}		
	},
	
	/**
	 * Formats DDDDamageTypes as plain text, with no colour information.
	 */
	STANDARD{
		@Override
		public String format(DDDDamageType t) {
			return t.getDisplayName();
		}		
	};
}
