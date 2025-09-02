package yeelp.distinctdamagedescriptions.integration.enderskills.dist;

import arekkuusu.enderskills.common.skill.ModAbilities;
import arekkuusu.enderskills.common.skill.SkillHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.AbstractSingleTypeDist;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class EnderSkillsThornyDistribution extends AbstractSingleTypeDist {

	public EnderSkillsThornyDistribution() {
		super("Ender Skills Thorny", Source.BUILTIN, () -> ModConfig.compat.enderskills.useThornyDistribution);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.PIERCING;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		Entity attacker = source.getTrueSource();
		if(attacker == null || !(source instanceof EntityDamageSource)) {
			return false;
		}
		if(!((EntityDamageSource) source).getIsThornsDamage() && !source.damageType.equals(DamageSource.CACTUS.damageType)) {
			return false;				
		}
		if(!SkillHelper.isActive(attacker, ModAbilities.THORNY)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int priority() {
		return 1;
	}

}
