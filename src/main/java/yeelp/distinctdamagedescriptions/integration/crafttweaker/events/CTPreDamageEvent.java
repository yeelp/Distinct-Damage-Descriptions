package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public class CTPreDamageEvent extends CTDDDEvent implements IPre {
	public CTPreDamageEvent(DamageDescriptionEvent.Pre evt) {
		super(evt);
	}
}
