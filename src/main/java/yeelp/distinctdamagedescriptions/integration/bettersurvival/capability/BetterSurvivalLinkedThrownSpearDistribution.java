package yeelp.distinctdamagedescriptions.integration.bettersurvival.capability;

import com.mujmajnkraft.bettersurvival.entities.projectiles.EntityFlyingSpear;

import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.LinkedDamageDistribution;

public final class BetterSurvivalLinkedThrownSpearDistribution extends LinkedDamageDistribution {

	private final String item;
	private IDamageDistribution distRef;
	
	public BetterSurvivalLinkedThrownSpearDistribution(String item) {
		this.item = item;
	}
	
	
	@Override
	protected IDamageDistribution getDamageDistribution() {
		return this.distRef != null ? this.distRef : DDDConfigurations.items.getOrFallbackToDefault(this.item);
	}

	/**
	 * @implNote The linked spear distribution has no need to copy, so it returns itself.
	 */
	@Override
	public IDamageDistribution copy() {
		return this;
	}
	
	@Override
	public IDamageDistribution update(IProjectile owner) {
		if(owner instanceof EntityFlyingSpear) {
			this.distRef = DDDAPI.accessor.getDamageDistribution(((EntityFlyingSpear) owner).getSpear()).orElse(null);
		}
		return this;
	}

}
