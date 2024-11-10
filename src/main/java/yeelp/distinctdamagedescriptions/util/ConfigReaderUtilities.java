package yeelp.distinctdamagedescriptions.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.IDDDRegistry;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * Contains some useful utilities for config readers
 * 
 * @author Yeelp
 *
 */
public final class ConfigReaderUtilities {

	private static final String LIST_SPLIT_REGEX = "\\),(?:\\s?)\\(";
	public static final String DDD_TYPE_REGEX = "ddd_[a-zA-Z]+";
	public static final String DAMAGE_TYPE_SUBREGEX = "([spb]|(" + DDD_TYPE_REGEX + "))";
	public static final String DECIMAL_REGEX = "\\d+(\\.\\d+)?";
	public static final String POTENTIALLY_NEGATIVE_DECIMAL_REGEX = "-?\\d+(\\.\\d+)?";
	public static final String ONLY_POSITIVE_ENTRY_TUPLE_SUBREGEX = "\\(" + DAMAGE_TYPE_SUBREGEX + ",\\s?" + DECIMAL_REGEX + "\\)";
	public static final String ALLOW_NEGATIVE_ENTRY_TUPLE_SUBREGEX = "\\(" + DAMAGE_TYPE_SUBREGEX + ",\\s?" + POTENTIALLY_NEGATIVE_DECIMAL_REGEX + "\\)";
	public static final String COMMENT_REGEX = "^(\\/\\/.*)?$";
	public static final String DIST_REGEX = buildListRegex(ONLY_POSITIVE_ENTRY_TUPLE_SUBREGEX);
	public static final String DIST_REGEX_ALLOW_EMPTY = buildListRegex(ONLY_POSITIVE_ENTRY_TUPLE_SUBREGEX, true);
	public static final String EMPTY_DIST = "\\[\\]";
	
	private ConfigReaderUtilities() {
		throw new UnsupportedOperationException("Utilities can't be instatiated!");
	}
	
	/**
	 * Return false if this string is a comment or blank line. Useful for filtering with Predicates.
	 * @param s The input string
	 * @return True if it matches {@link #COMMENT_REGEX}
	 */
	public static boolean isCommentEntry(String s) {
		return s.matches(COMMENT_REGEX);
	}

	/**
	 * Validate an object is non null. This differs from
	 * {@link Objects#requireNonNull(Object, Supplier)} in that it throws generic
	 * exceptions, allowing client code to specify the thrown exception type.
	 * 
	 * @param <U>          The type of the object to check
	 * @param <X>          The type of exception thrown
	 * @param u            the object to validate
	 * @param exceptionSup The generic exception {@link Supplier}
	 * @return The object if non null
	 * @throws X The exception returned by {@code exceptionSup.get()} if
	 *           {@code u == null}
	 */
	public static <U, X extends Exception> U validateNonNull(U u, Supplier<X> exceptionSup) throws X {
		if(u != null) {
			return u;
		}
		throw exceptionSup.get();
	}

	/**
	 * Parse a string as a map
	 *
	 * @param <K>          The keys of the map
	 * @param <V>          The values of the map
	 * @param reader       The reader this config entry came from
	 * @param s            The map to parse, which should match a regex returned by
	 *                     {@link #buildListRegex(String)}.
	 * @param kParser      The function that parses keys of the map. Should always
	 *                     return a non null value.
	 * @param vParser      The function that parses values of the map
	 * @param defaultValue The default value for the {@link NonNullMap}
	 * @return The NonNullMap populated with the entries specified by {@code s}
	 * @throws ConfigParsingException If {@code s} can't be parsed as a map, or
	 *                                {@code kParser} returns {@code null} for any
	 *                                parsed key
	 */
	public static <K, V> NonNullMap<K, V> parseMap(DDDConfigReader reader, String s, Function<String, K> kParser, Function<String, V> vParser, Supplier<V> defaultValue) throws ConfigParsingException {
		return buildMap(defaultValue, parseStringAsListOfEntries(reader, s, kParser, vParser));
	}

	/**
	 * Parse a damage type from config
	 * 
	 * @param s the damage type to parse
	 * @return {@link DDDBuiltInDamageType#SLASHING},
	 *         {@link DDDBuiltInDamageType#PIERCING} or
	 *         {@link DDDBuiltInDamageType#BLUDGEONING} if {@code s} equals
	 *         {@code "s"}, {@code "p"}, or {@code "b"}, respectively. If neither of
	 *         those conditions are true, The damage type is searched for in the
	 *         registries using {@code s} as a key.
	 * @see IDDDRegistry#get(String)
	 */
	public static DDDDamageType parseDamageType(String s) {
		switch(s) {
			case "s":
				return DDDBuiltInDamageType.SLASHING;
			case "p":
				return DDDBuiltInDamageType.PIERCING;
			case "b":
				return DDDBuiltInDamageType.BLUDGEONING;
			default:
				return DDDRegistries.damageTypes.get(s);
		}
	}

	/**
	 * Build regex for searching in a list. The regex will match strings that are
	 * comma separated lists of strings matched by {@code listEntrySubRegex}, with
	 * the whole list surrounded in brackets. This won't match empty lists
	 * 
	 * @param listEntrySubRegex the sub regex to use.
	 * @return the corresponding regex
	 */
	public static String buildListRegex(String listEntrySubRegex) {
		return buildListRegex(listEntrySubRegex, false);
	}

	/**
	 * Build regex for searching in a list. The regex will match strings that are
	 * comma separated lists of strings matched by {@code listEntrySubRegex}, with
	 * the whole list surrounded in brackets. This <em>WILL</em> match empty lists
	 * if {@code allowEmpty} is {@code true}.
	 * 
	 * @param listEntrySubRegex the subregex to use
	 * @param allowEmpty        if empty lists should be matched
	 * @return the corresponding subregex
	 */
	public static String buildListRegex(String listEntrySubRegex, boolean allowEmpty) {
		String start = "\\[" + (allowEmpty ? "(" : "");
		String end = (allowEmpty ? ")?" : "") + "\\]";
		return start + listEntrySubRegex + "(,\\s?" + listEntrySubRegex + ")*" + end;
	}

	private static <K, V> NonNullMap<K, V> buildMap(Supplier<V> defaultValue, Iterable<Entry<K, V>> mappings) {
		if(mappings == null) {
			return new NonNullMap<K, V>(defaultValue);
		}
		NonNullMap<K, V> map = new NonNullMap<K, V>(defaultValue);
		mappings.forEach((e) -> map.put(e.getKey(), e.getValue()));
		return map;
	}

	private static <K, V> Iterable<Entry<K, V>> parseStringAsListOfEntries(DDDConfigReader reader, String s, Function<String, K> kParser, Function<String, V> vParser) throws ConfigParsingException {
		// s is of the form [(t, a), (t, a), ... , (t, a)]
		if(s.equals("[]")) {
			return Collections.emptyList();
		}
		// can't simply use Stream ops; we potentially throw checked
		// exceptions and want to throw that up the call stack
		Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
		for(String str : s.substring(2, s.length() - 2).split(LIST_SPLIT_REGEX)) {
			String[] arr = str.split(",");
			result.add(new SimpleEntry<K, V>(validateNonNull(kParser.apply(arr[0].trim()), () -> new ConfigParsingException(reader.getName(), s)), vParser.apply(arr[1].trim())));
		}
		return result;
	}
	
}
