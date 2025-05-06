package yeelp.distinctdamagedescriptions.util.development;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel.DifficultyScaling;

public final class LivingAttackEventInfo extends AbstractLivingEventDeveloperModeInfo<LivingAttackEvent> {

	public LivingAttackEventInfo() {
		super(() -> ModConfig.dev.showAttackInfo);
	}
	
	@Override
	public boolean shouldFire(LivingAttackEvent evt) {
		return !(evt.getSource().getImmediateSource() == null && evt.getSource().getTrueSource() == null && evt.getSource().damageType.equals("generic") && evt.getAmount() == 0);
	}

	@Override
	protected StringBuilder getInfo(LivingAttackEvent evt, StringBuilder sb) {
		DamageSource src = evt.getSource();
		String direct = mapIfNonNullElseGetDefault(src.getImmediateSource(), AbstractDeveloperModeInfo::getEntityNameAndID, "None");
		String indirect = mapIfNonNullElseGetDefault(src.getTrueSource(), AbstractDeveloperModeInfo::getEntityNameAndID, "None");
		sb.append(String.format("ATTACK: Direct Attacker: %s | Attacker: %s | Defender: %s | Source: %s | Current Damage: %.2f %s", direct, indirect, evt.getEntityLiving().getName(), src.damageType, evt.getAmount(), DifficultyScaling.getVanillaDifficultyScaling(src, evt.getEntityLiving()).getScaledDamage(evt.getAmount())));
		return sb;
	}
}
