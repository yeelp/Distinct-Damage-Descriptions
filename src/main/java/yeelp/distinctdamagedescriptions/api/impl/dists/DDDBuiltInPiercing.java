package yeelp.distinctdamagedescriptions.api.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class DDDBuiltInPiercing extends AbstractSingleTypeDist {
	public DDDBuiltInPiercing() {
		super("builtInPiercing", Source.BUILTIN, () -> ModConfig.dmg.extraDamage.enableGuardianSpikesDamage);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.PIERCING;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage() && source.getTrueSource() instanceof EntityGuardian;
	}
}
