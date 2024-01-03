package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IWorld;
import yeelp.distinctdamagedescriptions.event.AssignMobResistancesEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities.CTResistances;

public class CTAssignMobResistancesEvent implements IDDDAssignMobResistancesEvent {

	private final IEntityLivingBase entity;
	private final IWorld world;
	private final CTResistances resistances;
	
	public CTAssignMobResistancesEvent(AssignMobResistancesEvent evt) {
		this.entity = CraftTweakerMC.getIEntityLivingBase(evt.getEntityLivingBase());
		this.world = CraftTweakerMC.getIWorld(evt.getWorld());
		this.resistances = new CTResistances(this.entity);
	}
	
	@Override
	public IEntityLivingBase getEntityLivingBase() {
		return this.entity;
	}

	@Override
	public CTResistances getResistances() {
		return this.resistances;
	}

	@Override
	public IWorld getWorld() {
		return this.world;
	}

}
