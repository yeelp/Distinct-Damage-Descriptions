package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.damage.IDamageSource;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.classification.DDDClassificationEvent;

/**
 * A skeletal CT classification event implementation
 * 
 * @author Yeelp
 *
 * @param <Event> the kind of DDDClassificationEvent this CT event represents.
 */
public abstract class CTDDDClassificationEvent<Event extends DDDClassificationEvent> extends CTDDDEvent implements IDDDClassificationEvent {

	protected final Event internal;

	public CTDDDClassificationEvent(Event evt) {
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

}
