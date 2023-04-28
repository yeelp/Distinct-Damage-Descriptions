package yeelp.distinctdamagedescriptions.util.filtersystem;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbstractFilterOperation implements FilterOperation {

	private boolean negate = false;

	@Override
	public final boolean test(EntityLivingBase entity) {
		return this.negate != this.getTestResult(entity); //simple XOR
	}
	
	public abstract boolean getTestResult(EntityLivingBase entity);
	
	@Override
	public final void setNegateStatus(boolean shouldNegate) {
		this.negate = shouldNegate;
	}
}
