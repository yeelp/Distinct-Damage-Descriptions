package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

public final class CTShieldBlockEvent extends CTDDDCalculationEvent<ShieldBlockEvent> implements IShieldBlockEvent {

	public CTShieldBlockEvent(ShieldBlockEvent evt) {
		super(evt);
	}

	@Override
	public IItemStack getShield() {
		return CraftTweakerMC.getIItemStack(this.internal.getShield());
	}

	@Override
	public float getShieldEffectivenessForType(ICTDDDDamageType type) {
		return this.internal.getShieldDistribution().getWeight(type.asDDDDamageType());
	}

	@Override
	public void setShieldEffectivenessForType(ICTDDDDamageType type, float amount) {
		this.internal.getShieldDistribution().setWeight(type.asDDDDamageType(), amount);
	}

	@Override
	public void setCanceled(boolean status) {
		this.internal.setCanceled(status);
	}
}
