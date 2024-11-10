package yeelp.distinctdamagedescriptions.integration.baubles.modifier;

import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;

public final class BaubleDamageModifier extends AbstractBaubleModifier<DetermineDamageEvent> {

	@Override
	public void respondToEvent(DetermineDamageEvent evt) {
		this.forEach((type, weight) -> evt.setDamage(type, Math.max(0, evt.getDamage(type) * (1 + weight))));
	}

	@Override
	public BaubleModifierType getType() {
		return BaubleModifierType.DAMAGE_MOD;
	}

}
