package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;

public final class CTGatherDefensesEvent extends CTDDDClassificationEvent<GatherDefensesEvent> implements IGatherDefensesEvent {

	public CTGatherDefensesEvent(GatherDefensesEvent evt) {
		super(evt);
	}

	@Override
	public float getResistance(String type) {
		return this.internal.getResistance(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void setResistance(String type, float amount) {
		this.internal.setResistance(CTDDDEvent.parseDamageType(type), amount);
	}

	@Override
	public boolean hasImmunity(String type) {
		return this.internal.hasImmunity(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void grantImmunity(String type) {
		this.internal.addImmunity(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void revokeImmunity(String type) {
		this.internal.removeImmunity(CTDDDEvent.parseDamageType(type));
	}

	@Override
	public void clearImmunities() {
		this.internal.clearImmunities();
	}

	@Override
	public void clearResistances() {
		this.internal.clearResistances();
	}

}
