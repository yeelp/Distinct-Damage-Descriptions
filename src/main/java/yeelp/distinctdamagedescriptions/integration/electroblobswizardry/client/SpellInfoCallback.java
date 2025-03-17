package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import electroblob.wizardry.util.IElementalDamage;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.development.AbstractLivingEventDeveloperModeInfo;

public final class SpellInfoCallback extends AbstractLivingEventDeveloperModeInfo<LivingAttackEvent> {

	public SpellInfoCallback() {
		super(() -> ModConfig.compat.ebwizardry.showSpellDamageInfo);
	}
	
	@Override
	public boolean shouldFire(LivingAttackEvent evt) {
		return evt.getSource() instanceof IElementalDamage;
	}

	@Override
	protected StringBuilder getInfo(LivingAttackEvent evt, StringBuilder sb) {
		return sb.append("SPELL DAMAGE TYPE: "+((IElementalDamage) evt.getSource()).getType().toString());
	}

}
