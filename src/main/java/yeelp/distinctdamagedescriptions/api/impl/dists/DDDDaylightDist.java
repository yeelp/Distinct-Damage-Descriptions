package yeelp.distinctdamagedescriptions.api.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class DDDDaylightDist extends AbstractSingleTypeDist {
	
	public static final String NAME = "daylight";
	
	public DDDDaylightDist() {
		super(NAME, Source.BUILTIN, () -> ModConfig.dmg.extraDamage.enableDaylightBurningDamage);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.RADIANT;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		if(source == DamageSource.ON_FIRE && target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			return DDDRegistries.trackers.isTracking(NAME, target);
		}
		return false;
	}

	@Override
	public int priority() {
		return 1;
	}
}
