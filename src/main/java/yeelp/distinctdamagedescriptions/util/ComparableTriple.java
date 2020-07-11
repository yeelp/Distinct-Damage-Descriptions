package yeelp.distinctdamagedescriptions.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * A comparable triple. The type arguments must be comparable, but don't have to be the same type.
 * <p>
 * The Iterable iterates over the items from left to right as Comparables. Use type checking and safe casting!
 * @author Yeelp
 *
 * @param <U> The left object of the triple. Must be a Comparable
 * @param <V> The middle object of the triple. Must be a Comparable
 * @param <W> The right object of the triple. Must be a Comparable
 */
public class ComparableTriple<U extends Comparable, V extends Comparable, W extends Comparable> implements Iterable<Comparable>, Comparable<ComparableTriple>
{
	private U left;
	private V middle;
	private W right;

	/**
	 * Build a new ComparableTriple
	 * @param left
	 * @param middle
	 * @param right
	 */
	public ComparableTriple(U left, V middle, W right)
	{
		this.left = left;
		this.middle = middle;
		this.right = right;
	}
	
	/**
	 * Get the left element of this ComparableTriple
	 * @return the left element
	 */
	public U getLeft()
	{
		return this.left;
	}
	
	/**
	 * Get the middle element of this ComparableTriple
	 * @return the middle element
	 */
	public V getMiddle()
	{
		return this.middle;
	}
	
	/**
	 * Get the right element of this ComparableTriple
	 * @return the right element
	 */
	public W getRight()
	{
		return this.right;
	}
	
	/**
	 * Set the left element of this ComparableTriple
	 * @param left element to set
	 */
	public void setLeft(U left)
	{
		this.left = left;
	}
	
	/**
	 * Set the middle element of this ComparableTriple
	 * @param middle element to set
	 */
	public void setMiddle(V middle)
	{
		this.middle = middle;
	}
	
	/**
	 * Set the right element of this ComparableTriple
	 * @param right element to set
	 */
	public void setRight(W right)
	{
		this.right = right;
	}
	
	/**
	 * The ComparableTriple enforces a lexicographical order on members. That is, 
	 * the statement {@code (u, v, w) < (u', v', w')} if and only if one of the following holds:<p>
	 *    -{@code u < u'}<p>
	 *    -{@code u = u'} and {@code v < v'}<p>
	 *    -{@code u = u'} and {@code v = v'} and {@code w < w'}
	 * <p>
	 * This ordering results in a partial order on ComparableTriples. 
	 * If however, each of the {@link Comparable#compareTo} methods of the Comparable types U, V, W 
	 * enforce total orders, the the resulting order of the ComparableTriple is also a total order.
	 * <p> 
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(ComparableTriple otherTriple)
	{
		int compareU = this.left.compareTo(otherTriple.left);
		if(compareU == 0)
		{
			int compareV = this.middle.compareTo(otherTriple.middle);
			if(compareV == 0)
			{
				return this.right.compareTo(otherTriple.right);
			}
			else
			{
				return compareV;
			}
		}
		else
		{
			return compareU;
		}
	}

	@Override
	public Iterator<Comparable> iterator()
	{
		return new TripleIterator(this);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ComparableTriple)
		{
			ComparableTriple other = (ComparableTriple) o;
			return this.left.equals(other.left) && this.middle.equals(other.middle) && this.right.equals(other.right);
		}
		else
		{
			return false;
		}
	}

	private static class TripleIterator implements Iterator<Comparable>
	{
		Stack<Comparable> elements;
		TripleIterator(ComparableTriple triple)
		{
			elements = new Stack<Comparable>();
			elements.push(triple.right);
			elements.push(triple.middle);
			elements.push(triple.left);
		}
		@Override
		public boolean hasNext()
		{
			return !elements.isEmpty();
		}
		@Override
		public Comparable next()
		{
			if(hasNext())
			{
				return elements.pop();
			}
			else
			{
				throw new NoSuchElementException("Iteration tried to iterate over more elements after already iterating over them all!");
			}
		}
	}
}
