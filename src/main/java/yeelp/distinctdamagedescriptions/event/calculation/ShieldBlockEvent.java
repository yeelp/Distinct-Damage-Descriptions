package yeelp.distinctdamagedescriptions.event.calculation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;

@Cancelable
public final class ShieldBlockEvent extends DDDCalculationEvent {

	private final ItemStack shield;
	private final ShieldDistribution shieldDist;
	public ShieldBlockEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap map, ItemStack shield) {
		super(attacker, trueAttacker, defender, src, map);
		this.shield = shield;
		this.shieldDist = DDDAPI.accessor.getShieldDistribution(this.getShield());
	}

	public ItemStack getShield() {
		return this.shield;
	}
	
	public ShieldDistribution getShieldDistribution() {
		return this.shieldDist;
	}
}
