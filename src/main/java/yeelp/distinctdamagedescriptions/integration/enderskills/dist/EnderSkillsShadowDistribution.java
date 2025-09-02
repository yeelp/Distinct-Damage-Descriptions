package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

import arekkuusu.enderskills.common.skill.ModAbilities;
import arekkuusu.enderskills.common.skill.SkillHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class EnderSkillsShadowDistribution extends SimpleEnderSkillsPredefinedDistribution {
	
	public EnderSkillsShadowDistribution() {
		super("shadow", () -> ModConfig.compat.enderskills.shadowDist, () -> DefaultValues.SHADOW_DISTRIBUTION);
	}

	@Override
	protected final boolean isApplicable(DamageSource src, EntityLivingBase target) {
		return super.isApplicable(src, target) && SkillHelper.isActiveFrom(src.getTrueSource(), ModAbilities.SHADOW);
	}
}
