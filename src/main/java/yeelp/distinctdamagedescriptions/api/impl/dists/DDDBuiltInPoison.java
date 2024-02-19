package yeelp.distinctdamagedescriptions.api.impl.dists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

public final class DDDBuiltInPoison extends AbstractSingleTypeDist {
	public DDDBuiltInPoison() {
		super("builtInPoison", Source.BUILTIN, () -> ModConfig.dmg.extraDamage.enablePoisonEffectDamage);
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.POISON;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		if(source == DamageSource.MAGIC && target.isPotionActive(MobEffects.POISON)) {
			PotionEffect effect = target.getActivePotionEffect(MobEffects.POISON);
			if(effect.getPotion().isReady(effect.getDuration(), effect.getAmplifier())) {
				return true;
			}
		}
		return false;
	}
}
