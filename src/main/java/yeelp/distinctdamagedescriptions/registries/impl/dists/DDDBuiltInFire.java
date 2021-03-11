package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class DDDBuiltInFire extends AbstractSingleTypeDist
{
	private static final Set<DamageSource> validSources = Sets.newHashSet(Lists.newArrayList(DamageSource.HOT_FLOOR, DamageSource.IN_FIRE, DamageSource.LAVA, DamageSource.ON_FIRE));
	private DDDDaylightDist daylight;
	public DDDBuiltInFire()
	{
		super(() -> ModConfig.dmg.extraDamage.enableFireDamage);
		daylight = (DDDDaylightDist) DDDRegistries.distributions.get("daylight");
	}

	@Override
	protected DDDDamageType getType()
	{
		return DDDBuiltInDamageType.FIRE;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target)
	{
		if(daylight.enabled() && !daylight.useType(source, target))
		{
			return false;
		}
		return validSources.contains(source);
	}

	@Override
	public String getName()
	{
		return "builtInFire";
	}
}
