package yeelp.distinctdamagedescriptions.util.development;

import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public final class UpdateAdaptiveResistanceEventInfo extends AbstractDDDCalcEventDeveloperModeInfo<UpdateAdaptiveResistanceEvent> {

	public UpdateAdaptiveResistanceEventInfo() {
		super(() -> ModConfig.dev.showAdaptiveCalculation);
	}
	
	@Override
	protected StringBuilder getInfo(UpdateAdaptiveResistanceEvent evt, StringBuilder sb) {
		sb.append("ADAPTIVE: ");
		switch(evt.getResult()) {
			case DEFAULT:
				if(!DDDAPI.accessor.getMobResistances(evt.getDefender()).filter(IMobResistances::hasAdaptiveResistance).isPresent()) {
					sb.append(String.format("Defender %s isn't adaptive, and won't adapt.", evt.getDefender().getName()));
					break;
				}
			case ALLOW:
				sb.append(String.format("Adaptability allowed for %s", evt.getDefender().getName())).append(NEW_LINE).append("Adapting to types: ");
				sb.append(YLib.joinNiceString(true, ",", evt.getDamageToAdaptTo().keySet().stream().map(DDDDamageType::getDisplayName).toArray(String[]::new)));
				break;
			default:
				sb.append(String.format("Adaptability disallowed for %s", evt.getDefender().getName()));
				break;
		}
		return sb;
	}

}
