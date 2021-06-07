package yeelp.distinctdamagedescriptions.api;

import javax.annotation.Nullable;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDFontColour;

/**
 * Basic skeleton of a DDD damage type.
 * 
 * @author Yeelp
 *
 */
public interface DDDDamageType {
	/**
	 * The Type of a DDDDamage type. Physical or Special
	 * 
	 * @author Yeelp
	 *
	 */
	public enum Type {
		PHYSICAL,
		SPECIAL;

		@Override
		public String toString() {
			switch(this) {
				case PHYSICAL:
					return "Physical";
				case SPECIAL:
					return "Special";
				default:
					return "";
			}
		}
	}

	/**
	 * Get the name of this type, with the "ddd_" prefix
	 * 
	 * @return the type name, with the "ddd_" prefix.
	 */
	String getTypeName();

	/**
	 * Get the display name for this DDDDamageType, without colour
	 * 
	 * @return the display name
	 */
	String getDisplayName();
	
	/**
	 * Get the display name of this damage type for use in tooltips. Combines the colour of the type with the display name.
	 * @return the formatted display name
	 */
	default String getFormattedDisplayName() {
		return DDDFontColour.encodeColour(getColour())+getDisplayName().replaceAll(" ", DDDFontColour.Marker.SPACE.replaceWith())+DDDFontColour.Marker.END.getC();
	}

	/**
	 * Get the base damage distribution for this type
	 * 
	 * @return and IDistribution that distributes the damage to the type returned by
	 *         {@link #getTypeName()}
	 */
	IDamageDistribution getBaseDistribution();

	/**
	 * Get the type of this damage
	 * 
	 * @return a Type enum, that is either PHYSICAL or SPECIAL
	 */
	Type getType();

	/**
	 * Get the death message for this damage type
	 * 
	 * @param hasAttacker true if the damage type had an attacker
	 * @return the corresponding death message
	 */
	@Nullable String getDeathMessage(boolean hasAttacker);

	/**
	 * Is this damage type a custom defined one, or built in?
	 * 
	 * @return true if user defined, false if built in.
	 */
	boolean isCustomDamage();

	/**
	 * Get the colour used when displaying this type name in tooltips
	 * 
	 * @return the hex colour as an int.
	 */
	int getColour();
}
