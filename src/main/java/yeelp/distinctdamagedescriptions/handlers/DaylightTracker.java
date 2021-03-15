package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DaylightTracker extends AbstractTracker
{
	@Override
	public boolean shouldStartTracking(EntityLivingBase entity)
	{
		return entity.world.isDaytime() && entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && entity.isBurning() && entity.world.canBlockSeeSky(entity.getPosition());
	}

	@Override
	public boolean shouldStopTracking(EntityLivingBase entity)
	{
		return !entity.isBurning();
	}
}
