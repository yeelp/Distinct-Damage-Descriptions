package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.DamageCalculationEvent;

public class CTPostDamageEvent extends CTDDDEvent implements IPost {
	public CTPostDamageEvent(DamageCalculationEvent.Post evt) {
		super(evt);
	}
}
