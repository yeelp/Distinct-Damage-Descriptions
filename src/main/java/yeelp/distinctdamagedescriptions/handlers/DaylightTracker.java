package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DaylightTracker extends Handler
{
	private static final Set<UUID> tracking = new HashSet<UUID>();
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent evt)
	{
		EntityLivingBase entity = evt.getEntityLiving();
		boolean burning = entity.isBurning();
		UUID id = entity.getUniqueID();
		if(tracking.contains(id) && !burning)
		{
			tracking.remove(id);
		}
		else
		{
			if(entity.world.isDaytime() && entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && burning && entity.world.canBlockSeeSky(entity.getPosition()))
			{
				tracking.add(id);
			}
		}
	}
	
	public static boolean isBurning(UUID id)
	{
		return tracking.contains(id);
	}
}
