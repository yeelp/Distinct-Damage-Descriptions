package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

public final class BaubleBruteForceModifier extends AbstractBaubleModifier<GatherDefensesEvent> {

	@Override
	public void respondToEvent(GatherDefensesEvent evt) {
		this.forEach((type, weight) -> evt.setResistance(type, Math.max(0, evt.getResistance(type) - weight)));
	}

	@Override
	public BaubleModifierType getType() {
		return BaubleModifierType.BRUTE_FORCE;
	}

}
