package yeelp.distinctdamagedescriptions.registries.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.handlers.DDDTrackers;
import yeelp.distinctdamagedescriptions.handlers.DaylightTracker;

public final class DDDDaylightDist extends AbstractSingleTypeDist
{
	public DDDDaylightDist()
	{
		super(() -> ModConfig.dmg.extraDamage.enableDaylightBurningDamage);
	}

	@Override
	protected DDDDamageType getType()
	{
		return DDDBuiltInDamageType.RADIANT;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target)
	{
		if(source == DamageSource.ON_FIRE && target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
		{
			return DDDTrackers.daylight.isTracking(target.getUniqueID());
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getName()
	{
		return "daylight";
	}

}
