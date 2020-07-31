package yeelp.distinctdamagedescriptions.handlers;

import net.minecraftforge.event.entity.living.PotionEvent;
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
}
