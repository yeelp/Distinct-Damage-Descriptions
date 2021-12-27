package yeelp.distinctdamagedescriptions.registries.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class DDDBuiltInFire extends AbstractSingleTypeDist {

	public DDDBuiltInFire() {
		super(() -> ModConfig.dmg.extraDamage.enableFireDamage);
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
	public String getName() {
		return "builtInFire";
	}

	@Override
	public int priority() {
		return -1; // want this last as a default "catch all" case for fire damage.
	}
}
