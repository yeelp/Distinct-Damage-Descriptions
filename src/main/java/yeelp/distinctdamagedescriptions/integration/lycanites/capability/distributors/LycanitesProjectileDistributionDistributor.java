package yeelp.distinctdamagedescriptions.integration.lycanites.capability.distributors;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;

import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.integration.lycanites.capability.LycanitesProjectileDistribution;

public class LycanitesProjectileDistributionDistributor extends AbstractCapabilityDistributor<IProjectile, IDamageDistribution, IDamageDistribution> {
	private static LycanitesProjectileDistributionDistributor instance;

	protected LycanitesProjectileDistributionDistributor() {
		super(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	}

	@Override
	public boolean isApplicable(IProjectile t) {
		return t instanceof BaseProjectileEntity;
	}

	@Override
	protected IDamageDistribution getCapability(IProjectile t, String key) {
		return new LycanitesProjectileDistribution().update(t);
	}

	@Override
	protected IDDDConfiguration<IDamageDistribution> getConfig() {
		return DDDConfigurations.projectiles;
	}

	public static LycanitesProjectileDistributionDistributor getInstance() {
		return instance == null ? instance = new LycanitesProjectileDistributionDistributor() : instance;
	}
}
