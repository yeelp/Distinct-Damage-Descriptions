package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;

/**
 * Generic mob filter
 * @author Yeelp
 *
 */
public interface IDDDFilter {

	/**
	 * Get an Iterable of all the filters being applied.
	 * @return An Iterable of {@link FilterOperation}s
	 */
	Collection<FilterOperation> getAppliedFilters();
	
	/**
	 * Get the way this filter is applied; either as a base or modifier
	 * @return The filter application type.
	 */
	FilterApplicationType getApplicationType();
	
	/**
	 * Get the method the filters should be combined.
	 * @return the filter combination method.
	 */
	FilterCombinationMethod getCombinationMethod();
	
	/**
	 * Check if the entity passes the filter
	 * @param entity target to check
	 * @return An Optional wrapping the result; empty if the entity did not pass the filter.
	 */
	default Optional<EntityLivingBase> filter(@Nonnull EntityLivingBase entity) {
		return Optional.of(entity).filter((e) -> this.getCombinationMethod().test(this.getAppliedFilters().stream(), e));
	}
	
	enum FilterCombinationMethod implements BiPredicate<Stream<FilterOperation>, EntityLivingBase> {
		OR(Stream::anyMatch),
		AND(Stream::allMatch);
		
		private BiPredicate<Stream<FilterOperation>, Predicate<? super FilterOperation>> method;
		
		private FilterCombinationMethod(BiPredicate<Stream<FilterOperation>, Predicate<? super FilterOperation>> f) {
			this.method = f;
		}
		
		@Override
		public boolean test(Stream<FilterOperation> s, EntityLivingBase e) {
			return this.method.test(s, (f) -> f.test(e));
		}
	}
}
