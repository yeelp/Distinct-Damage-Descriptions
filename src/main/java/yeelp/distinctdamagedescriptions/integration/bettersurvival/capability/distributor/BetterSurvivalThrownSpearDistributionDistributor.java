package yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.distributor;

import com.mujmajnkraft.bettersurvival.entities.projectiles.EntityFlyingSpear;

import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.bettersurvival.capability.BetterSurvivalThrownSpearDistribution;

public final class BetterSurvivalThrownSpearDistributionDistributor extends AbstractCapabilityDistributor<IProjectile, IDamageDistribution, IDamageDistribution> {

	protected BetterSurvivalThrownSpearDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	}
	
	private static BetterSurvivalThrownSpearDistributionDistributor instance;

	@Override
	public boolean isApplicable(IProjectile t) {
		return t instanceof EntityFlyingSpear;
	}

	@Override
	protected IDamageDistribution getCapability(IProjectile t, String key) {
		return new BetterSurvivalThrownSpearDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static BetterSurvivalThrownSpearDistributionDistributor getInstance() {
		return instance == null ? instance = new BetterSurvivalThrownSpearDistributionDistributor() : instance;
	}
}
