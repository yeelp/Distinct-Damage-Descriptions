package yeelp.distinctdamagedescriptions.util.development;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class ShieldBlockEventInfo extends AbstractDDDCalcEventDeveloperModeInfo<ShieldBlockEvent> {

	public ShieldBlockEventInfo() {
		super(() -> ModConfig.dev.showShieldCalculation);
	}
	
	@Override
	protected StringBuilder getInfo(ShieldBlockEvent evt, StringBuilder sb) {
		sb.append("SHIELD: ");
		if(evt.isCanceled()) {
			sb.append(String.format("Shield blocking disabled for %s", evt.getDefender().getName()));
		}
		else {
			sb.append("Shield Distribution").append(NEW_LINE);
			DDDRegistries.damageTypes.getAll().stream().filter((type) -> evt.getShieldDistribution().getWeight(type) > 0).map((type) -> String.format("%s: %.2f%n", type.getDisplayName(), evt.getShieldDistribution().getWeight(type))).forEach(sb::append);
		}
		return sb;
	}

}
