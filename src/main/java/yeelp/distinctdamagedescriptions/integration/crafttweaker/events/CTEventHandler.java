package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class CTEventHandler extends Handler
{
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void preDamage(DamageDescriptionEvent.Pre evt)
	{
		CTDDDEventManager.PRE_DAMAGE.publish(new CTPreDamageEvent(evt));
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void postDamage(DamageDescriptionEvent.Post evt)
	{
		CTDDDEventManager.POST_DAMAGE.publish(new CTPostDamageEvent(evt));
	}
}