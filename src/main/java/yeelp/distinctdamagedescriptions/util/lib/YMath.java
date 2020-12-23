package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection of useful math methods
 * @author Yeelp
 *
 */
public final class YMath
{
	/**
	 * Sum a collection of numbers.
	 * @param <T> The type of numbers being summed.
	 * @param values the collection to sum
	 * @return a double representation of the sum. Each value will be cast to a double via {@link Number#doubleValue()}
	 */
	public static final <T extends Number> double sum(Collection<T> values)
	{
		return values.stream().reduce(0.0d, (u, v) -> u + v.doubleValue(), (u, v) -> u + v);
	}
	
	/**
	 * Performs set minus on two Sets. Specifically, {@code setMinus(a, b)}, written as a - b, is the set {{@code x | a.contains(x) && !b.contains(x)}}.
	 * @param <X> The type of members of the sets
	 * @param a set A
	 * @param b set B
	 * @return a new Set that is the result of the set minus operation a - b.
	 */
	public static final <X> Set<X> setMinus(Set<X> a, Set<X> b)
	{
		HashSet<X> result = new HashSet<X>(a);
		for(X x : a) //iterate over a, so removing elements from result won't cause problems by removing during iteration.
		{
			{
				result.remove(x);
			}
		}
		return result;
	}
}
