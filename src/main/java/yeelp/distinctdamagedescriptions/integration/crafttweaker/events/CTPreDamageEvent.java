package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.DamageCalculationEvent;

public class CTPreDamageEvent extends CTDDDEvent implements IPre {
	public CTPreDamageEvent(DamageCalculationEvent.Pre evt) {
		super(evt);
	}
}
