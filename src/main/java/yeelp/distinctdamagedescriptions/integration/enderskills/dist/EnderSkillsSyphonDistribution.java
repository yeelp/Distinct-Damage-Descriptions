package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

import arekkuusu.enderskills.api.event.SkillDamageSource;
import arekkuusu.enderskills.common.skill.ability.BaseAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.enderskills.handler.EnderSkillsHandler;

public final class EnderSkillsSyphonDistribution extends EnderSkillsPredefinedDistribution {

	public EnderSkillsSyphonDistribution() {
		super("Ender Skills Syphon", () -> ModConfig.compat.enderskills.syphonDist, () -> DefaultValues.SYPHON_DISTRIBUTION);
	}

	@Override
	protected boolean isApplicable(DamageSource src, EntityLivingBase target) {
		Entity attacker = src.getTrueSource();
		if(attacker == null || !(attacker instanceof EntityLivingBase)) {
			return false;
		}
		EntityLivingBase owner = (EntityLivingBase) attacker;
		//negate isPresent check for if a skill was used this tick; Syphon doesn't fire SkillDamageEvent. This is a catch all at the end.
		return src instanceof SkillDamageSource && src.damageType.equals(BaseAbility.DAMAGE_HIT_TYPE) && !EnderSkillsHandler.getLastKnownSkillUsed(src, owner).filter((sur) -> sur.didOwnerUseThisTick(owner)).isPresent();
	}
	
	@Override
	public boolean enabled() {
		return ModConfig.compat.enderskills.useSeparateSyphonDist;
	}
	
	@Override
	public int priority() {
		return -1;
	}

}
