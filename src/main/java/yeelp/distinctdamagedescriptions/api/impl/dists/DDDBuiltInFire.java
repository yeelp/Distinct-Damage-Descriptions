package yeelp.distinctdamagedescriptions.api.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class DDDBuiltInFire extends AbstractSingleTypeDist {

	public DDDBuiltInFire() {
		super("builtInFire", Source.BUILTIN, () -> ModConfig.dmg.extraDamage.enableFireDamage);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.FIRE;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source.isFireDamage();
	}

	@Override
	public int priority() {
		return Integer.MIN_VALUE; // want this last as a default "catch all" case for fire damage.
	}
}
