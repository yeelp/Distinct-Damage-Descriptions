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

	public static final EventList<CTDetermineDamageEvent> DETERMINE_DAMAGE = newEventList();
	public static final EventList<CTGatherDefensesEvent> GATHER_DEFENSES = newEventList();
	public static final EventList<CTShieldBlockEvent> SHIELD_BLOCK = newEventList();
	public static final EventList<CTUpdateAdaptiveResistancesEvent> UPDATE_ADAPTIVE = newEventList();
	
	@ZenMethod
	public static IEventHandle onDetermineDamage(IEventHandler<CTDetermineDamageEvent> handler) {
		return DETERMINE_DAMAGE.add(handler);
	}
	
	@ZenMethod
	public static IEventHandle onGatherDefenses(IEventHandler<CTGatherDefensesEvent> handler) {
		return GATHER_DEFENSES.add(handler);
	}
	
	@ZenMethod
	public static IEventHandle onShieldBlock(IEventHandler<CTShieldBlockEvent> handler) {
		return SHIELD_BLOCK.add(handler);
	}
	
	@ZenMethod
	public static IEventHandle onUpdateAdaptiveResistances(IEventHandler<CTUpdateAdaptiveResistancesEvent> handler) {
		return UPDATE_ADAPTIVE.add(handler);
	}
	
	private static <T extends IDDDEvent> EventList<T> newEventList() {
		return new EventList<T>();
	}
}
