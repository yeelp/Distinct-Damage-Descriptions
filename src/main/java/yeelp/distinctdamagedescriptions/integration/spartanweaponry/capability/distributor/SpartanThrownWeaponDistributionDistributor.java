package yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability.distributor;

import com.oblivioussp.spartanweaponry.entity.projectile.EntityThrownWeapon;

import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability.SpartanThrownWeaponDistribution;

public class SpartanThrownWeaponDistributionDistributor extends AbstractCapabilityDistributor<IProjectile, IDamageDistribution, IDamageDistribution> {

	protected SpartanThrownWeaponDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	}

	private static SpartanThrownWeaponDistributionDistributor instance;

	@Override
	public boolean isApplicable(IProjectile t) {
		return t instanceof EntityThrownWeapon;
	}

	@Override
	protected IDamageDistribution getCapability(IProjectile t, String key) {
		return new SpartanThrownWeaponDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return null;
	}

	public static SpartanThrownWeaponDistributionDistributor getInstance() {
		return instance == null ? instance = new SpartanThrownWeaponDistributionDistributor() : instance;
	}

}
