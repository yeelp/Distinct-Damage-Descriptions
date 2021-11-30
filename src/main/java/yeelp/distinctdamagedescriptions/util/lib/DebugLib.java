package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Map;
import java.util.Map.Entry;

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
}
