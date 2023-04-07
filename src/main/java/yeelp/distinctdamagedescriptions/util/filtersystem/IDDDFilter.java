package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.Optional;

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
	Iterable<FilterOperation> getAppliedFilters();
	
	/**
	 * Get the way this filter is applied; either as a base or modifier
	 * @return The filter application type.
	 */
	FilterApplicationType getApplicationType();
	
	/**
	 * Check if the entity passes the filter
	 * @param entity target to check
	 * @return An Optional wrapping the result; empty if the entity did not pass the filter.
	 */
	default Optional<EntityLivingBase> filter(@Nonnull EntityLivingBase entity) {
		for(FilterOperation f : this.getAppliedFilters()) {
			if(!f.test(entity)) {
				return Optional.empty();
			}
		}
		return Optional.of(entity);
	}
}
