package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.event.CustomDamageEvent;
import yeelp.distinctdamagedescriptions.event.PhysicalDamageEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

@ZenClass("mods.ddd.events.DDDEvents")
@ZenRegister
public final class CTDDDEventManager
{
	public static final EventList<CTPhysicalDamageEvent> PHYSICAL_DAMAGE = new EventList<CTPhysicalDamageEvent>();
	public static final EventList<CTCustomDamageEvent> CUSTOM_DAMAGE = new EventList<CTCustomDamageEvent>();
	
	@ZenMethod
	public static IEventHandle onPhysicalDamage(IEventHandler<CTPhysicalDamageEvent> handler)
	{
		return PHYSICAL_DAMAGE.add(handler);
	}
	
	@ZenMethod
	public static IEventHandle onCustomDamage(IEventHandler<CTCustomDamageEvent> handler)
	{
		return CUSTOM_DAMAGE.add(handler);
	}
}
