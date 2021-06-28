package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;

public final class DaylightTracker extends AbstractTracker {
	@Override
	public boolean shouldStartTracking(EntityLivingBase entity) {
		return entity.world.isDaytime() && entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && entity.isBurning() && entity.world.canBlockSeeSky(entity.getPosition());
	}

	@Override
	public boolean shouldStopTracking(EntityLivingBase entity) {
		return !entity.isBurning();
	}
}
