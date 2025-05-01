package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * A collection of useful math methods
 * 
 * @author Yeelp
 *
 */
public final class YMath {
	/**
	 * Sum a collection of numbers.
	 * 
	 * @param <T>    The type of numbers being summed.
	 * @param values the collection to sum
	 * @return a double representation of the sum. Each value will be cast to a
	 *         double via {@link Number#doubleValue()}
	 */
	public static final <T extends Number> double sum(Collection<T> values) {
		return values.stream().mapToDouble(Number::doubleValue).sum();
	}

	/**
	 * Performs set union on two Sets. Specifically, {@code setUnion(a, b)} is the
	 * set {{@code x | a.contains(x) || b.contains(x)}}
	 * 
	 * @param <X> the type of the members of the sets
	 * @param a   set A
	 * @param b   set B
	 * @return a new Set that is the result of the set union of a and b
	 */
	public static final <X> Set<X> setUnion(Set<X> a, Set<X> b) {
		HashSet<X> result = new HashSet<X>(a);
		for(X x : b) {
			result.add(x);
		}
		return result;
	}

	/**
	 * Checks set equality on two Sets. Specifically, this checks if
	 * {@code a.containsAll(b) && b.containsAll(a)}.
	 * 
	 * @param <X> the type of members of the sets
	 * @param a   set A
	 * @param b   set B
	 * @return True if the sets are equal in terms of set equality.
	 */
	public static final <X> boolean setEquals(Set<X> a, Set<X> b) {
		return a.containsAll(b) && b.containsAll(a);
	}

	/**
	 * Performs set difference on two Sets. Specifically, this returns a new set
	 * where {{@code x | a.contains(x) && !b.contains(x)}}
	 * 
	 * @param <X> the type of members of the sets
	 * @param a   set A
	 * @param b   set B
	 * @return a new Set that is the set difference of a and b.
	 */
	public static final <X> Set<X> setDifference(Set<X> a, Set<X> b) {
		HashSet<X> result = Sets.newHashSet(a);
		b.forEach(result::remove);
		return result;
	}
}