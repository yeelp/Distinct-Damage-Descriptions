package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.List;

import javax.annotation.Nullable;

/**
 * A tooltip formatter. Can format an ItemStack, adding DDD specific tooltip information
 * @author Yeelp
 * @param <T> The type of objects this TooltipFormatter formats with
 *
 */
public interface TooltipFormatter<T> {
	
	/**
	 * Does this TooltipFormatter support this DDDNumberFormatter?
	 * @param f formatter to check
	 * @return true is supported, false if not.
	 */
	boolean supportsNumberFormat(DDDNumberFormatter f);
	
	/**
	 * Does this TooltipFormatter support this DDDDamageFormatter
	 * @param f formatter to check
	 * @return true if supported, false if not.
	 */
	boolean supportsDamageFormat(DDDDamageFormatter f);
	
	/**
	 * Set this TooltipFormatter's DDDNumberFormatter
	 * @param f formatter to set
	 * @throws IllegalArgumentException if this formatter doesn't support this DDDNumberFormatter
	 */
	void setNumberFormatter(DDDNumberFormatter f);
	
	/**
	 * Set this TooltipFormatter's DDDDamageFormatter
	 * @param f formatter to set
	 * @throws IllegalArgumentException if this formatter doesn't support this DDDDamageFormatter
	 */
	void setDamageFormatter(DDDDamageFormatter f);
	
	/**
	 * Format information for an ItemStack
	 * @param t the object to format for
	 * @return A List of Strings to add to this stack's tooltip
	 */
	List<String> format(@Nullable T t);
	
	/**
	 * Get the type strings this formatter creates. Used for ordering.
	 * @return The type
	 */
	TooltipOrder getType();
	
	/**
	 * The type of strings created
	 * @author Yeelp
	 *
	 */
	enum TooltipOrder {
		DAMAGE,
		MOB_DAMAGE,
		PROJECTILE,
		ARMOR,
		SHIELD,
		MOB_RESISTANCES;
	}
	
}
