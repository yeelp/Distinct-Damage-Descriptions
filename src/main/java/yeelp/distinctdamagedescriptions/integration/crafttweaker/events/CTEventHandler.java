package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.event.CustomDamageEvent;
import yeelp.distinctdamagedescriptions.event.PhysicalDamageEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class CTEventHandler extends Handler
{
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void physicalDamage(PhysicalDamageEvent evt)
	{
		CTDDDEventManager.PHYSICAL_DAMAGE.publish(new CTPhysicalDamageEvent(evt));
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void customDamage(CustomDamageEvent evt)
	{
		CTDDDEventManager.CUSTOM_DAMAGE.publish(new CTCustomDamageEvent(evt));
	}
}