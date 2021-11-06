package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import net.minecraftforge.fml.common.eventhandler.Event.Result;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;

public final class CTUpdateAdaptiveResistancesEvent extends CTDDDCalculationEvent<UpdateAdaptiveResistanceEvent> implements IUpdateAdaptiveResistances {

	public CTUpdateAdaptiveResistancesEvent(UpdateAdaptiveResistanceEvent evt) {
		super(evt);
	}

	@Override
	public float getResistance(String type) {
		return this.internal.getResistance(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public boolean hasImmunity(String type) {
		return this.internal.hasImmunity(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void ignoreType(String type) {
		this.internal.ignoreType(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void setResult(Result result) {
		this.internal.setResult(result);
	}

}
