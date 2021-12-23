package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A skeletal implementation of a tracker, Tracks entities on a
 * LivingUpdateEvent
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractTracker extends Handler {
	private final Set<UUID> tracking = new HashSet<UUID>();

	@SubscribeEvent
	public final void onEntityUpdate(final LivingUpdateEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		UUID id = entity.getUniqueID();
		if(isTracking(id) && shouldStopTracking(entity)) {
			this.tracking.remove(id);
		}
		else if(shouldStartTracking(entity)) {
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
		return isTracking(entity.getUniqueID());
	}
}
