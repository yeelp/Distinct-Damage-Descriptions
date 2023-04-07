package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.function.Predicate;

import com.google.common.base.Predicates;

import net.minecraft.entity.EntityLivingBase;

public abstract class SimpleFilterOperation extends AbstractFilterOperation {

	private final Predicate<EntityLivingBase> p;
	
	protected SimpleFilterOperation(Predicate<EntityLivingBase> p) {
		this.p = p;
	}
	
	@Override
	public final boolean getTestResult(EntityLivingBase entity) {
		return this.p.test(entity);
	}

	public static final class IsBossFilter extends SimpleFilterOperation {
		public IsBossFilter() {
			super(Predicates.not(EntityLivingBase::isNonBoss));
		}		
	}
	
}
