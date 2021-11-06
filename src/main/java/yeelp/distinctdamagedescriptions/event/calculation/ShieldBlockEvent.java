package yeelp.distinctdamagedescriptions.event.calculation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;

/**
 * Fired when DDD computes shield reductions. If no shield calculations are to
 * be done (i.e. no active shield), this event doesn't fire at all.<br>
 * This event is {@link Cancelable}. <br>
 * When cancelled, shield calculations are not done. the shield's resources
 * (like durability) won't be consumed. <br>
 * This event does not have a result {@link HasResult}
 * 
 * @author Yeelp
 *
 */
@Cancelable
public final class ShieldBlockEvent extends DDDCalculationEvent {

	private final ItemStack shield;
	private final ShieldDistribution shieldDist;

	public ShieldBlockEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap map, ItemStack shield) {
		super(attacker, trueAttacker, defender, src, map);
		this.shield = shield;
		this.shieldDist = (ShieldDistribution) DDDAPI.accessor.getShieldDistribution(this.getShield()).copy();
	}

	/**
	 * Get the shield being used
	 * 
	 * @return The ItemStack that holds the shield being used. This ItemStack always
	 *         has a {@link ShieldDistribution} capability.
	 */
	public ItemStack getShield() {
		return this.shield;
	}

	/**
	 * Get the {@link ShieldDistribution} being used. Changes can be made here and
	 * it will affect the effectiveness of the shield for this calculation only.
	 * 
	 * @return The active {@link ShieldDistribution}
	 */
	public ShieldDistribution getShieldDistribution() {
		return this.shieldDist;
	}
}
