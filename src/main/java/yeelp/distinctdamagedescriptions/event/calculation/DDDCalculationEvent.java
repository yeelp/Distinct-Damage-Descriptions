package yeelp.distinctdamagedescriptions.event.calculation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.util.DamageMap;

/**
 * Fired when DDD does combat calculations. <br>
 * Some events are {@link Cancelable}. <br>
 * Some events have results {@link HasResult}. <br>
 * All children are fired on {@link MinecraftForge#EVENT_BUS}.
 * 
 * @author Yeelp
 *
 */
public abstract class DDDCalculationEvent extends Event {
	private final Entity attacker;
	private final Entity trueAttacker;
	private final EntityLivingBase defender;
	private final DamageSource src;
	protected final DamageMap dmg;

	protected DDDCalculationEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap dmg) {
		this.attacker = attacker;
		this.trueAttacker = trueAttacker;
		this.defender = defender;
		this.src = src;
		this.dmg = dmg;
	}

	/**
	 * Get the defending entity
	 * 
	 * @return The defending entity
	 */
	@Nonnull
	public final EntityLivingBase getDefender() {
		return this.defender;
	}

	/**
	 * Get the immediate attacking entity. The arrow, not the shooter.
	 * 
	 * @return the direct attacker. May not exist. Just because this doesn't exist,
	 *         doesn't mean {@link DDDCalculationEvent#getTrueAttacker()} is non
	 *         null.
	 */
	@Nullable
	public final Entity getImmediateAttacker() {
		return this.attacker;
	}

	/**
	 * Get the true source of the attack. The shulker, not the bullet.
	 * 
	 * @return The true attacker. May not exist.
	 */
	@Nullable
	public final Entity getTrueAttacker() {
		return this.trueAttacker;
	}

	/**
	 * Get the damage source that caused this attack
	 * 
	 * @return The original damage source.
	 */
	public final DamageSource getSource() {
		return this.src;
	}

	/**
	 * Get the calculated damage inflicted of a certain type. Damage can't be
	 * altered at this point as the damage has already been classified.
	 * 
	 * @param type Type
	 * @return The damage inflicted of that type
	 * @see DetermineDamageEvent
	 */
	public final float getDamage(DDDDamageType type) {
		return this.dmg.get(type);
	}
}
