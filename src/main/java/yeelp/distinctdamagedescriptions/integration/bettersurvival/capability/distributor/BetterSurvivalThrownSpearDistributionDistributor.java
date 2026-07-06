package yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.distributor;

import com.mujmajnkraft.bettersurvival.entities.projectiles.EntityFlyingSpear;
import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.BetterSurvivalThrownSpearDistribution;

import javax.annotation.Nonnull;

public final class BetterSurvivalThrownSpearDistributionDistributor extends AbstractCapabilityDistributor<IProjectile, IDamageDistribution, IDamageDistribution> {

	private BetterSurvivalThrownSpearDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	}
	
	private static BetterSurvivalThrownSpearDistributionDistributor instance;

	@Override
	public boolean isApplicable(IProjectile t) {
		return t instanceof EntityFlyingSpear;
	}

	@Override
	protected IDamageDistribution getCapability(@Nonnull IProjectile t, @Nonnull String key) {
		return new BetterSurvivalThrownSpearDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}
	
	public static BetterSurvivalThrownSpearDistributionDistributor getInstance() {
		return instance == null ? instance = new BetterSurvivalThrownSpearDistributionDistributor() : instance;
	}
}
