package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability;

import java.util.Optional;

import net.minecraft.entity.IProjectile;
import slimeknights.tconstruct.library.capability.projectile.TinkerProjectileHandler;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.LinkedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.TiCUtil;

public final class TinkersLinkedProjectileDistribution extends LinkedDamageDistribution {

	private IDamageDistribution ref;
	
	@Override
	protected IDamageDistribution getDamageDistribution() {
		return this.ref == null ? new DamageDistribution() : this.ref;
	}

	@Override
	public IDamageDistribution copy() {
		return this;
	}
	
	@Override
	public IDamageDistribution update(IProjectile owner) {
		this.ref = TiCUtil.getProjectileHandler(owner).flatMap(TinkersLinkedProjectileDistribution::getDamageDistributionFromHandler).orElse(null);
		return this;
	}
	
	private static Optional<IDamageDistribution> getDamageDistributionFromHandler(TinkerProjectileHandler handler) {
		return DDDAPI.accessor.getDamageDistribution(handler.getItemStack());
	}

}
