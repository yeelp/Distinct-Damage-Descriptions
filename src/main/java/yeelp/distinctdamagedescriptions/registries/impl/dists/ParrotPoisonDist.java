package yeelp.distinctdamagedescriptions.registries.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.handlers.DDDTrackers;

public class ParrotPoisonDist extends AbstractSingleTypeDist {
	public ParrotPoisonDist() {
		super(() -> ModConfig.dmg.extraDamage.enableParrotPoisonDamage);
	}

	@Override
	public String getName() {
		return "parrotPoison";
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.POISON;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return DDDTrackers.parrot.wasPoisonedByCookie(target.getUniqueID());
	}

}
