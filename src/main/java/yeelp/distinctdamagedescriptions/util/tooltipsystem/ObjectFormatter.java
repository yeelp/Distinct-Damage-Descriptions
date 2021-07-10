package yeelp.distinctdamagedescriptions.util.tooltipsystem;

/**
 * Formats an individual object to a String
 * @author Yeelp
 *
 * @param <T> The type of object this ObjectFormatter formats
 */
public interface ObjectFormatter<T> {
	
	/**
	 * Format an object to a String
	 * @param t the object
	 * @return a formatted String
	 */
	String format(T t);
}
