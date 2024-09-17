package yeelp.distinctdamagedescriptions.api;

import java.util.Arrays;

import javax.annotation.Nullable;

import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DDDFontColour;

/**
 * Basic skeleton of a DDD damage type.
 * 
 * @author Yeelp
 *
 */
public interface DDDDamageType extends Comparable<DDDDamageType>, IHasCreationSource {
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
	 * A prefix that DDD uses to recognize types it adds.
	 */
	static String DDD_PREFIX = "ddd_";

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
	 * Get the display name of this damage type for use in tooltips. Combines the
	 * colour of the type with the display name.
	 * 
	 * @return the formatted display name
	 */
	default String getFormattedDisplayName() {
		return DDDFontColour.encodeColour(getColour()) + getDisplayName().replaceAll(" ", String.valueOf(DDDFontColour.Marker.SPACE.getC())) + DDDFontColour.Marker.END.getC();
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
	@Nullable
	String getDeathMessage(boolean hasAttacker);

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

	/**
	 * Is this damage type usable?
	 * 
	 * @return If this damage type is allowed to be used in game.
	 */
	default boolean isUsable() {
		return true;
	}
	
	/**
	 * Is this damage type hidden? Hidden types don't show up in tooltips. Missing distributions for damage distributions get listed under @link {@link DDDBuiltInDamageType#UNKNOWN}.
	 * @return if this type should be hidden in tooltips or not.
	 */
	boolean isHidden();
	
	/**
	 * Hide this type from appearing on tooltips. Missing distributions for damage distributions get listed under @link {@link DDDBuiltInDamageType#UNKNOWN} and will show up, even if the Unknown type is itself a hidden type.
	 */
	void hideType();
	
	/**
	 * Unhide this type so it shows up in tooltips.
	 */
	void unhideType();

	/**
	 * Add the DDD prefix to a damage type, or really to any string, but only if the prefix doesn't exist.
	 * @param s
	 * @return a String with {@link #DDD_PREFIX} added to the front if it isn't present.
	 */
	static String addDDDPrefixIfNeeded(String s) {
		return s.startsWith(DDD_PREFIX) ? s : DDD_PREFIX.concat(s);
	}
	
	/**
	 * Remove the DDD prefix from a damage type string, or really any string, but only if it is present to begin with. 
	 * @param s
	 * @return a String with {@link #DDD_PREFIX} removed from the front if it is present.
	 */
	static String removeDDDPrefixIfPresent(String s) {
		return s.startsWith(DDD_PREFIX) ? s.substring(DDD_PREFIX.length()) : s;
	}
	
	/**
	 * Is the type an internal type? Internal types are used by DDD and should not be used by external sources.
	 * @param type
	 * @return True if {@code type} is an internal type.
	 */
	static boolean isInternalType(DDDDamageType type) {
		return Arrays.binarySearch(DDDBuiltInDamageType.INTERNAL_TYPES, type) >= 0;
	}
	
	/**
	 * Is this type the unknown damage type?
	 * @param type
	 * @return true if it is {@link DDDBuiltInDamageType#UNKNOWN}
	 */
	static boolean isUnknownType(DDDDamageType type) {
		return DDDBuiltInDamageType.UNKNOWN.equals(type);
	}
}
