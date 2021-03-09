package yeelp.distinctdamagedescriptions.api;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

public interface DDDSingleTypeDistribution extends DDDPredefinedDistribution
{
	/**
	 * Classify a DamageSource against this built in distribution. Return the correct DDDDamageType if it's classifies as this dist, otherwise return {@link DDDBuiltInDamageType#NORMAL}
	 * @param source
	 * @param target
	 * @return
	 */
	@Nonnull
	DDDDamageType classify(DamageSource source, EntityLivingBase target);
}
