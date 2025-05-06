package yeelp.distinctdamagedescriptions.util.development;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel.DifficultyScaling;

public final class DetermineDamageEventInfo extends AbstractDDDClassificationEventDeveloperModeInfo<DetermineDamageEvent> {

	public DetermineDamageEventInfo() {
		super(() -> ModConfig.dev.showDamageClassification);
	}

	@Override
	protected StringBuilder getInfo(DetermineDamageEvent evt, StringBuilder sb) {
		sb.append("DETERMINE DAMAGE: Damage Classification").append(NEW_LINE);
		DifficultyScaling diff = DifficultyScaling.getVanillaDifficultyScaling(evt.getSource(), evt.getDefender());
		DDDRegistries.damageTypes.getAll().stream().filter((type) -> evt.getDamage(type) > 0).map((type) -> String.format("%s: %.2f %s%n", type.getDisplayName(), evt.getDamage(type), diff.getScaledDamage(evt.getDamage(type)))).forEach(sb::append);
		return sb;
	}

}
