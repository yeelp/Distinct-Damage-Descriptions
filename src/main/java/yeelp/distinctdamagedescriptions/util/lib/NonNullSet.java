package yeelp.distinctdamagedescriptions.util.lib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple Set that doesn't permit null values.
 * 
 * @author Yeelp
 *
 * @param <E> The types of objects in the set.
 */
public class NonNullSet<E> extends HashSet<E> implements Set<E> {

	/**
	 * Creates a new NonNullSet from the specified elements, removing null elements from the argument iterable.
	 * @param <T> the type of elements in the iterable.
	 * @param elements the iterable.
	 * @return The constructed NonNullSet.
	 */
	public static <T> NonNullSet<T> newNonNullSetRemoveNulls(Iterable<T> elements) {
		NonNullSet<T> set = new NonNullSet<>();
		elements.forEach(set::addIfNonNull);
		return set;
	}

	public static <T> NonNullSet<T> newNonNullSet() {
		return new NonNullSet<>();
	}

	public static <T> NonNullSet<T> newNonNullSet(Iterable<T> elements) {
		return new NonNullSet<>(elements);
	}
	
	/**
	 * Constructs a new NonNullSet.
	 */
	public NonNullSet() {
		super();
	}

	/**
	 * Constructs a new NonNullSet containing the specified elements. If any of the
	 * specified elements are {@code null} the constructor throws an
	 * {@link UnsupportedOperationException}
	 * 
	 * @param elements the elements that will be in the constructed set.
	 * @throws UnsupportedOperationException If any of the objects in
	 *                                       {@code elements} are null.
	 */
	public NonNullSet(Iterable<E> elements) {
		super();
		elements.forEach(this::add);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method throws a {@link UnsupportedOperationException} if the passed
	 * argument is null.
	 * 
	 * @throws UnsupportedOperationException if the argument is null.
	 */
	@Override
	public boolean add(E e) {
		this.checkIfNull(e);
		return super.add(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method throws a {@link UnsupportedOperationException} if any of the
	 * elements in the collection is null.
	 * 
	 * @throws UnsupportedOperationException if any of the elements in the
	 *                                       collection is null.
	 */
	@Override
	public boolean addAll(@Nonnull Collection<? extends E> c) {
		return super.addAll(c);
	}

	/**
	 * Adds an element to this NonNullSet only if it is not null. Unlike
	 * {@link #add(Object)}, this will not throw an exception if {@code null} is
	 * passed as an argument; the method merely returns {@code false}.
	 * 
	 * @param e the element to add.
	 * @return true if the set was altered by this method call, false if the set was
	 *         unchanged (which may have been the result of the argument being
	 *         null).
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean addIfNonNull(@Nullable E e) {
		if(e != null) {
			return super.add(e);
		}
		return false;
	}

	private void checkIfNull(E e) {
		if(e == null) {
			throw new UnsupportedOperationException("Null objects not allowed in NonNullSet");
		}
	}
}
