package yeelp.distinctdamagedescriptions.integration.spartanweaponry.capability;

import com.oblivioussp.spartanweaponry.entity.projectile.EntityThrownWeapon;

import net.minecraft.entity.IProjectile;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.LinkedDamageDistribution;

public final class SpartanLinkedThrownWeaponDistribution extends LinkedDamageDistribution {

	private final String item;
	private IDamageDistribution distRef;
	
	public SpartanLinkedThrownWeaponDistribution(String s) {
		this.item = s;
	}
	
	@Override
	protected IDamageDistribution getDamageDistribution() {
		return this.distRef != null ? this.distRef : DDDConfigurations.items.getOrFallbackToDefault(this.item);
	}

	/**
	 * @implNote The SpartanLinkedThrownWeaponDistribution has no need to copy, so it just returns itself.
	 */
	@Override
	public IDamageDistribution copy() {
		return this;
	}
	
	@Override
	public IDamageDistribution update(IProjectile owner) {
		if(owner instanceof EntityThrownWeapon) {
			this.distRef = DDDAPI.accessor.getDamageDistribution(((EntityThrownWeapon) owner).getWeaponStack()).orElse(null);
		}
		return this;
	}

}
