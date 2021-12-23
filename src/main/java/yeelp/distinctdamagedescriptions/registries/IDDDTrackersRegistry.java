package yeelp.distinctdamagedescriptions.registries;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.handlers.AbstractTracker;

public interface IDDDTrackersRegistry extends IDDDRegistry<AbstractTracker> {
	default boolean isTracking(String type, EntityLivingBase entity) {
		return this.get(type).isTracking(entity);
	}
}
