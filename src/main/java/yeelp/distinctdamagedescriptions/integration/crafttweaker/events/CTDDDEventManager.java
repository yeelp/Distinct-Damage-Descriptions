package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ddd.events.DDDEvents")
@ZenRegister
public final class CTDDDEventManager {
	public static final EventList<CTPreDamageEvent> PRE_DAMAGE = new EventList<CTPreDamageEvent>();
	public static final EventList<CTPostDamageEvent> POST_DAMAGE = new EventList<CTPostDamageEvent>();

	@ZenMethod
	public static IEventHandle onPreDamage(IEventHandler<CTPreDamageEvent> handler) {
		return PRE_DAMAGE.add(handler);
	}

	@ZenMethod
	public static IEventHandle onPostDamage(IEventHandler<CTPostDamageEvent> handler) {
		return POST_DAMAGE.add(handler);
	}
}
