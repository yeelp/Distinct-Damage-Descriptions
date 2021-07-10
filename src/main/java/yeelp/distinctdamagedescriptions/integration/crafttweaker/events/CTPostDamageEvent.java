package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public class CTPostDamageEvent extends CTDDDEvent implements IPost {
	public CTPostDamageEvent(DamageDescriptionEvent.Post evt) {
		super(evt);
	}
}
