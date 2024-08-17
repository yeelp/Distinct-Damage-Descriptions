package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

//import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;

@stanhebben.zenscript.annotations.ZenClass(CTConsts.CTClasses.EVENTMANAGER)
@crafttweaker.annotations.ZenRegister
public final class CTDDDEventManager {

	public static final EventList<CTDetermineDamageEvent> DETERMINE_DAMAGE = newEventList();
	public static final EventList<CTGatherDefensesEvent> GATHER_DEFENSES = newEventList();
	public static final EventList<CTShieldBlockEvent> SHIELD_BLOCK = newEventList();
	public static final EventList<CTUpdateAdaptiveResistancesEvent> UPDATE_ADAPTIVE = newEventList();
	public static final EventList<CTAssignMobResistancesEvent> ASSIGN_RESISTS = new EventList<CTAssignMobResistancesEvent>();

	@stanhebben.zenscript.annotations.ZenMethod
	public static IEventHandle onDetermineDamage(IEventHandler<CTDetermineDamageEvent> handler) {
		return DETERMINE_DAMAGE.add(handler);
	}

	@stanhebben.zenscript.annotations.ZenMethod
	public static IEventHandle onGatherDefenses(IEventHandler<CTGatherDefensesEvent> handler) {
		return GATHER_DEFENSES.add(handler);
	}

	@stanhebben.zenscript.annotations.ZenMethod
	public static IEventHandle onShieldBlock(IEventHandler<CTShieldBlockEvent> handler) {
		return SHIELD_BLOCK.add(handler);
	}

	@stanhebben.zenscript.annotations.ZenMethod
	public static IEventHandle onUpdateAdaptiveResistances(IEventHandler<CTUpdateAdaptiveResistancesEvent> handler) {
		return UPDATE_ADAPTIVE.add(handler);
	}
	
	@stanhebben.zenscript.annotations.ZenMethod
	public static IEventHandle onAssignMobResistances(IEventHandler<CTAssignMobResistancesEvent> handler) {
		return ASSIGN_RESISTS.add(handler);
	}

	private static <T extends IDDDEvent> EventList<T> newEventList() {
		return new EventList<T>();
	}
}
