package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * A collection of useful methods
 * 
 * @author Yeelp
 *
 */
public final class YLib {
	/**
	 * Join an array of strings together nicely with a separator
	 * 
	 * @param addSpace true if a space will be added after every separator
	 * @param sep      the separator to use
	 * @param strings  the array of strings
	 * @return a String that is the result of nicely joining the {@code strings}
	 */
	public static String joinNiceString(boolean addSpace, String sep, String... strings) {
		return joinNiceString(addSpace, sep, Arrays.stream(strings));
	}
	
	/**
	 * Join a Stream or strings together nicely with a separator
	 * 
	 * @param addSpace true if a space will be added after every separator
	 * @param sep      the separator to use
	 * @param strings  the stream of strings
	 * @return a String that is the result of nicely joining the {@code strings}
	 */
	public static String joinNiceString(boolean addSpace, String sep, Stream<String> strings) {
		String sepUsed = addSpace ? sep.concat(" ") : sep;
		return strings.reduce((s1, s2) -> s1.concat(sepUsed).concat(s2)).orElse("");
	}

	/**
	 * Merge two arrays of Strings, by placing all elements of {@code ys} after all
	 * elements of {@code xs}. Does not sort them. Use standard sort methods from
	 * Arrays for that.
	 * 
	 * @param xs the first array
	 * @param ys the second array
	 * @return An array where all the elements of {@code xs} are followed by all the
	 *         elements of {@code ys}, in their original order.
	 */
	public static String[] merge(String[] xs, String[] ys) {
		if(xs.length == 0) {
			return ys;
		}
		else if(ys.length == 0) {
			return xs;
		}
		else {
			int index = -1;
			String[] arr = new String[xs.length + ys.length];
			for(String x : xs) {
				arr[++index] = x;
			}
			for(String y : ys) {
				arr[++index] = y;
			}
			return arr;
		}
	}

	/**
	 * Capitalize the first character in a String
	 * 
	 * @param s
	 * @return a new String with the first character capitalized.
	 */
	public static String capitalize(String s) {
		char[] cs = s.toCharArray();
		cs[0] = Character.toTitleCase(cs[0]);
		return new String(cs);
	}
}
