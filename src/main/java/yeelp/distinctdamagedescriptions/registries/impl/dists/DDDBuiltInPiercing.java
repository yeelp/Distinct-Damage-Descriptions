package yeelp.distinctdamagedescriptions.registries.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

public final class DDDBuiltInPiercing extends AbstractSingleTypeDist
{
	public DDDBuiltInPiercing()
	{
		super(() -> ModConfig.dmg.extraDamage.enableGuardianSpikesDamage);
	}

	@Override
	protected DDDDamageType getType()
	{
		return DDDBuiltInDamageType.PIERCING;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target)
	{
		return source.isMagicDamage() & source.damageType.equals("thorns") && source.getTrueSource() instanceof EntityGuardian;
	}

	@Override
	public String getName()
	{
		return "builtInPiercing";
	}
}
