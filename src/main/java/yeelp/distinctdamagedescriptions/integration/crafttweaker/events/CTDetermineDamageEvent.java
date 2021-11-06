package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;

public final class CTDetermineDamageEvent extends CTDDDClassificationEvent<DetermineDamageEvent> implements IDetermineDamageEvent {

	public CTDetermineDamageEvent(DetermineDamageEvent evt) {
		super(evt);
	}

	@Override
	public float getDamage(String type) {
		return this.internal.getDamage(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void setDamage(String type, float amount) {
		this.internal.setDamage(CTDDDEvent.parseDamageType(type), amount);
	}

}
