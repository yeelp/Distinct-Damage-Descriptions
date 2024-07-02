package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.ILivingEvent;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities.CTResistances;

@ZenClass(CTConsts.CTClasses.EVENTASSIGNRESISTANCES)
@ZenRegister
public interface IDDDAssignMobResistancesEvent extends ILivingEvent {
	@ZenGetter("resistances")
	CTResistances getResistances();
	
	@ZenGetter("world")
	IWorld getWorld();
}
