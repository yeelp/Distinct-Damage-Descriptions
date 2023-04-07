package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;

public interface FilterOperation extends Predicate<EntityLivingBase>{
	
	/**
	 * Check if the EntityLivingBase passes this filter.
	 * @param entity
	 * @return true if it passes, false if not.
	 */
	@Override
	boolean test(EntityLivingBase entity);
}
