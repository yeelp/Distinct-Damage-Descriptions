package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Map;
import java.util.Map.Entry;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.ModConfig;

/**
 * Organizes useful methods for debug info here
 * 
 * @author Yeelp
 *
 */
public final class DebugLib {
	private static boolean status = false;

	/**
	 * Enable this library. If turned off, all methods short circuit to return a
	 * default value. Disabled when {@link ModConfig#showDotsOn} is false, to avoid
	 * doing unnecessary computations.
	 */
	public static void updateStatus() {
		status = ModConfig.showDotsOn;
	}

	/**
	 * Converts a Map to a collection of Strings, specifically a String of the form
	 * {{@code entries}}, where {@code entries} is a comma separated list of the map
	 * entries, of the form {@code (key, value)},
	 * 
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values the keys map to
	 * @param map the map
	 * @return A String representing the entries in the map, in a nicely joined
	 *         String.
	 */
	public static final <K, V> String entriesToString(Map<K, V> map) {
		if(!status) {
			return "";
		}
		String[] strings = new String[map.size()];
		int i = 0;
		for(Entry<K, V> entry : map.entrySet()) {
			strings[i++] = String.format("(%s, %s)", entry.getKey().toString(), entry.getValue().toString());
		}
		return "{" + YLib.joinNiceString(true, ",", strings) + "}";
	}

	/**
	 * Output formatted debug info.
	 * 
	 * @param format  The format string.
	 * @param objects the object arguments.
	 * 
	 * @implNote status check needed as {@link String#format} doesn't need to run if not enabled.
	 */
	public static final void outputFormattedDebug(String format, Object... objects) {
		if(!status) {
			return;
		}
		DistinctDamageDescriptions.debug(String.format(format, objects));
	}

	/**
	 * Converts an Iterable of generic elements to a list of String of the form
	 * {@code [entries]} where {@code entries} is a comma separated list of the
	 * elements in the Iterable.
	 * 
	 * @param <T> The type of elements in the Iterable.
	 * @param it the Iterable.
	 * @return A String representing the elements of the Iterable.
	 */
	public static final <T> String iterableToString(Iterable<T> it) {
		if(!status) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		it.forEach((s) -> builder.append(s.toString() + ";"));
		String[] strings = builder.toString().split(";");
		return "[" + YLib.joinNiceString(true, ",", strings) + "]";
	}

	/**
	 * Run some Runnable. Useful to isolate debug code from running when debug mode
	 * is not enabled.
	 * 
	 * @param runnable
	 */
	public static final void doDebug(Runnable runnable) {
		if(!status) {
			return;
		}
		runnable.run();
	}
}
