package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.distributors;

import net.minecraft.entity.IProjectile;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerDamageDistribution;

public class TinkerProjectileCapabilityDistributor extends AbstractCapabilityDistributor<IProjectile, IDamageDistribution, IDamageDistribution> {

	private static TinkerProjectileCapabilityDistributor instance;

	protected TinkerProjectileCapabilityDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	}

	@Override
	public boolean isApplicable(IProjectile t) {
		return t instanceof EntityProjectileBase;
	}

	@Override
	protected IDamageDistribution getCapability(IProjectile t, String key) {
		return new TinkerDamageDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}

	public static TinkerProjectileCapabilityDistributor getInstance() {
		return instance == null ? instance = new TinkerProjectileCapabilityDistributor() : instance;
	}
}
