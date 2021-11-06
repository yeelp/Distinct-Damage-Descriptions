package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;

public final class CTShieldBlockEvent extends CTDDDCalculationEvent<ShieldBlockEvent> implements IShieldBlockEvent {

	public CTShieldBlockEvent(ShieldBlockEvent evt) {
		super(evt);
	}

	@Override
	public IItemStack getShield() {
		return CraftTweakerMC.getIItemStack(this.internal.getShield());
	}

	@Override
	public float getShieldEffectivenessForType(String type) {
		return this.internal.getShieldDistribution().getWeight(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void setShieldEffectivenessForType(String type, float amount) {
		this.internal.getShieldDistribution().setWeight(CTDDDEvent.parseDamageType(type), amount);
	}

	@Override
	public void setCanceled(boolean status) {
		this.internal.setCanceled(status);
	}	
}
