package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

public final class CTDetermineDamageEvent extends CTDDDClassificationEvent<DetermineDamageEvent> implements IDetermineDamageEvent {

	public CTDetermineDamageEvent(DetermineDamageEvent evt) {
		super(evt);
	}

	@Override
	public float getDamage(ICTDDDDamageType type) {
		return this.internal.getDamage(type.asDDDDamageType());
	}

	@Override
	public void setDamage(ICTDDDDamageType type, float amount) {
		this.internal.setDamage(type.asDDDDamageType(), amount);
	}

}
