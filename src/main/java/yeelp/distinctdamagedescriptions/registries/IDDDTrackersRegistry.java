package yeelp.distinctdamagedescriptions.registries;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.handlers.AbstractTracker;

import java.util.Objects;

public interface IDDDTrackersRegistry extends IDDDRegistry<AbstractTracker> {
	default boolean isTracking(String type, EntityLivingBase entity) {
		return Objects.requireNonNull(this.get(type)).isTracking(entity);
	}
}
