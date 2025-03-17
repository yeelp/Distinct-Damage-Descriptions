package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.dist;

import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.api.impl.dists.AbstractSingleTypeDist;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDDaylightDist;
import yeelp.distinctdamagedescriptions.handlers.DaylightTracker;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public final class UndeathBurningDistribution extends AbstractSingleTypeDist {
	
	public static final class UndeathBurningTracker extends DaylightTracker {
		
		public static final String NAME = "curse of undeath burning";
		
		@Override
		public boolean shouldStartTracking(EntityLivingBase entity) {
			return DaylightTracker.isInDaylight(entity) && entity.isPotionActive(WizardryPotions.curse_of_undeath);
		}
		
		@Override
		public String getName() {
			return NAME;
		}
	}
	
	public UndeathBurningDistribution() {
		super("wizardry undeath burning", Source.BUILTIN, () -> DDDRegistries.distributions.get(DDDDaylightDist.NAME).enabled());
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.RADIANT;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return source == DamageSource.ON_FIRE && DDDRegistries.trackers.isTracking(UndeathBurningTracker.NAME, target);
	}

}
