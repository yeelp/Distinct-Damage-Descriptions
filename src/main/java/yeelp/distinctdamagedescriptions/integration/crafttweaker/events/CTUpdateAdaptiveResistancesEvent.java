package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import net.minecraftforge.fml.common.eventhandler.Event.Result;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

public final class CTUpdateAdaptiveResistancesEvent extends CTDDDCalculationEvent<UpdateAdaptiveResistanceEvent> implements IUpdateAdaptiveResistances {

	public CTUpdateAdaptiveResistancesEvent(UpdateAdaptiveResistanceEvent evt) {
		super(evt);
	}

	@Override
	public float getResistance(ICTDDDDamageType type) {
		return this.internal.getResistance(type.asDDDDamageType());
	}

	@Override
	public boolean hasImmunity(ICTDDDDamageType type) {
		return this.internal.hasImmunity(type.asDDDDamageType());
	}

	@Override
	public void ignoreType(ICTDDDDamageType type) {
		this.internal.ignoreType(type.asDDDDamageType());
	}

	@Override
	public void setResult(Result result) {
		this.internal.setResult(result);
	}

	@Override
	public float getAdaptiveAmount() {
		return this.internal.getAdaptiveAmount();
	}

	@Override
	public void setAdaptiveAmount(float amount) {
		this.internal.setAdaptiveAmount(amount);
	}

}
