package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

public final class BaubleResistanceModifier extends AbstractBaubleModifier<GatherDefensesEvent> {

	@Override
	public void respondToEvent(GatherDefensesEvent evt) {
		this.forEach((type, weight) -> evt.setResistance(type, evt.getResistance(type) + weight));
	}

	@Override
	public BaubleModifierType getType() {
		return BaubleModifierType.RESISTANCE_MOD;
	}

}
