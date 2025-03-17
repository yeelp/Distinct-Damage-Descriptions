package yeelp.distinctdamagedescriptions.util.development;

import java.util.stream.Stream;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.Translations;

public class GatherDefensesEventInfo extends AbstractDDDClassificationEventDeveloperModeInfo<GatherDefensesEvent> {

	protected GatherDefensesEventInfo() {
		super(() -> ModConfig.dev.showDefenseClassification);
	}

	@Override
	protected StringBuilder getInfo(GatherDefensesEvent evt, StringBuilder sb) {
		sb.append("GATHER DEFENSES: Defense Classification").append(NEW_LINE);
		DDDRegistries.damageTypes.getAll().stream().flatMap((type) -> {
			Stream.Builder<String> stream = Stream.builder();
			String resistanceType = evt.hasResistance(type) ? Translations.INSTANCE.translate("tooltips", "resistance") : evt.hasWeakness(type) ? Translations.INSTANCE.translate("tooltips", "weakness") : "";
			if(!resistanceType.isEmpty()) {
				stream.accept(String.format("%s %s: %.2f%%", type.getDisplayName(), resistanceType, Math.abs(evt.getResistance(type)) * 100));
				stream.accept(evt.hasImmunity(type) ? " | " : NEW_LINE);
			}
			if(evt.hasImmunity(type)) {
				stream.accept(String.format("%s Immunity", type.getDisplayName()));
				stream.accept(NEW_LINE);
			}
			return stream.build();
		}).forEach(sb::append);
		return sb;
	}

}
