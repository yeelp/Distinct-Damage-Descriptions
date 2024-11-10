package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

public final class BaubleSlyStrikeModifier extends AbstractBaubleModifier<GatherDefensesEvent> {

	@Override
	public void respondToEvent(GatherDefensesEvent evt) {
		this.forEach((type, weight) -> {
			if(Math.random() < weight) {
				evt.removeImmunity(type);
			}
		});
	}

	@Override
	public BaubleModifierType getType() {
		return BaubleModifierType.SLY_STRIKE;
	}

}
