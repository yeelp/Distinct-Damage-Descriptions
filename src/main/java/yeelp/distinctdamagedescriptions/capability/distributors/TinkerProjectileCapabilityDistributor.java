package yeelp.distinctdamagedescriptions.capability.distributors;

import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

public final class TinkerProjectileCapabilityDistributor extends AbstractCapabilityDistributor<EntityProjectileBase, IDamageDistribution, IDamageDistribution> {

	protected TinkerProjectileCapabilityDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.LOC);
	}

	@Override
	public boolean isApplicable(EntityProjectileBase t) {
		return true;
	}

	@Override
	protected IDamageDistribution getCapability(EntityProjectileBase t, String key) {
		return DDDAPI.accessor.getDamageDistribution(t.tinkerProjectile.getItemStack());
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}

}
