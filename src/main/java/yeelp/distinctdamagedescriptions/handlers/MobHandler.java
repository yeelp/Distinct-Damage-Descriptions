package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;

public class MobHandler extends Handler
{
	@SubscribeEvent 
	public void onPotionApplyEvent(PotionEvent.PotionApplicableEvent evt)
	{
		evt.setResult(DDDAPI.accessor.getMobCreatureType(evt.getEntityLiving()).isImmuneToPotionEffect(evt.getPotionEffect()) ? Result.DENY : Result.DEFAULT);
	}
	
	@SubscribeEvent
	public void onCrit(CriticalHitEvent evt)
	{
		Entity target = evt.getTarget();
		if(target instanceof EntityLivingBase)
		{
			evt.setResult(DDDAPI.accessor.getMobCreatureType((EntityLivingBase) evt.getTarget()).isImmuneToCriticalHits() ? Result.DENY : Result.DEFAULT);
		}
	}
}
