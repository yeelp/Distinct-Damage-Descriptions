package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

public final class BaubleImmunityModifier extends AbstractBaubleModifier<GatherDefensesEvent> {

	@Override
	public void respondToEvent(GatherDefensesEvent evt) {
		this.forEach((type, weight) -> {
			if(Math.random() < weight) {
				evt.addImmunity(type);
			}
		});
	}

	@Override
	public BaubleModifierType getType() {
		return BaubleModifierType.IMMUNITY;
	}

}
