package yeelp.distinctdamagedescriptions.util.development;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class LivingDamageEventInfo extends AbstractLivingEventDeveloperModeInfo<LivingDamageEvent> {

	protected LivingDamageEventInfo() {
		super(() -> ModConfig.dev.showDamageInfo);
	}

	@Override
	public boolean shouldFire(LivingDamageEvent evt) {
		return true;
	}

	@Override
	protected StringBuilder getInfo(LivingDamageEvent evt, StringBuilder sb) {
		return sb.append(String.format("DAMAGE: Final damage amount for %s after resistances/immunities: %.2f", evt.getEntityLiving().getName(), evt.getAmount()));
	}
}
