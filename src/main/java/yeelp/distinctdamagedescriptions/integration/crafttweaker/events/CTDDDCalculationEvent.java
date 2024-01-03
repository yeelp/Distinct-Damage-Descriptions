package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.damage.IDamageSource;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.calculation.DDDCalculationEvent;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

/**
 * Skeletal implementation of DDD calculation events
 * 
 * @author Yeelp
 *
 * @param <Event> The underlying DDDCalculationEvent this wrapped CraftTweaker
 *                event represents
 */
public abstract class CTDDDCalculationEvent<Event extends DDDCalculationEvent> implements IDDDCalculationEvent {

	protected final Event internal;

	public CTDDDCalculationEvent(Event evt) {
		this.internal = evt;
	}

	@Override
	public IEntityLivingBase getDefender() {
		return CraftTweakerMC.getIEntityLivingBase(this.internal.getDefender());
	}

	@Override
	public IEntity getTrueAttacker() {
		return CraftTweakerMC.getIEntity(this.internal.getTrueAttacker());
	}

	@Override
	public IDamageSource getOriginalSource() {
		return CraftTweakerMC.getIDamageSource(this.internal.getSource());
	}

	@Override
	public IEntity getEntity() {
		return CraftTweakerMC.getIEntity(this.internal.getImmediateAttacker());
	}

	@Override
	public float getDamage(ICTDDDDamageType type) {
		return this.internal.getDamage(type.asDDDDamageType());
	}
}
