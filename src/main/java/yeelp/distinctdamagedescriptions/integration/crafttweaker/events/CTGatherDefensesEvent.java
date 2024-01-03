package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

public final class CTGatherDefensesEvent extends CTDDDClassificationEvent<GatherDefensesEvent> implements IGatherDefensesEvent {

	public CTGatherDefensesEvent(GatherDefensesEvent evt) {
		super(evt);
	}

	@Override
	public float getResistance(ICTDDDDamageType type) {
		return this.internal.getResistance(type.asDDDDamageType());
	}

	@Override
	public void setResistance(ICTDDDDamageType type, float amount) {
		this.internal.setResistance(type.asDDDDamageType(), amount);
	}

	@Override
	public boolean hasImmunity(ICTDDDDamageType type) {
		return this.internal.hasImmunity(type.asDDDDamageType());
	}

	@Override
	public void grantImmunity(ICTDDDDamageType type) {
		this.internal.addImmunity(type.asDDDDamageType());
	}

	@Override
	public void revokeImmunity(ICTDDDDamageType type) {
		this.internal.removeImmunity(type.asDDDDamageType());
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
