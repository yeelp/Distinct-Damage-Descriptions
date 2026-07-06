package yeelp.distinctdamagedescriptions.handlers;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;
import java.util.UUID;

/**
 * A skeletal implementation of a tracker, Tracks entities on a
 * LivingUpdateEvent
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractTracker extends Handler {
	private final Set<UUID> tracking = Sets.newHashSet();

	@SubscribeEvent
	public final void onEntityUpdate(final LivingUpdateEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		UUID id = entity.getUniqueID();
		if(this.isTracking(id) && this.shouldStopTracking(entity)) {
			this.tracking.remove(id);
		}
		else if(this.shouldStartTracking(entity)) {
			this.tracking.add(id);
		}
	}

	public abstract boolean shouldStartTracking(EntityLivingBase entity);

	public abstract boolean shouldStopTracking(EntityLivingBase entity);

	public abstract String getName();

	public final boolean isTracking(UUID id) {
		return this.tracking.contains(id);
	}

	public final boolean isTracking(EntityLivingBase entity) {
		return this.isTracking(entity.getUniqueID());
	}
}
