package yeelp.distinctdamagedescriptions.event.classification;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

/**
 * Event that gets fired when DDD classifies damage. This is where damage values
 * can be altered before real calculations are done. <br>
 * This event is not {@link Cancelable}. <br>
 * This event doesn't have a result {@link HasResult}.
 * 
 * @author Yeelp
 *
 */
public final class DetermineDamageEvent extends DDDClassificationEvent {

	private final DamageMap map;

	public DetermineDamageEvent(Entity attacker, Entity trueAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map) {
		super(attacker, trueAttacker, defender, src);
		this.map = Objects.requireNonNull(map, "Damage map can't be null!");
	}

	/**
	 * Get the damage inflicted of a certain type
	 * 
	 * @param type
	 * @return The damage inflicted of that type.
	 */
	public float getDamage(DDDDamageType type) {
		return this.map.get(type);
	}

	/**
	 * Set the damage to inflict of a certain type.
	 * 
	 * @param type
	 * @param amount The amount of damage to inflict. Must be non-negative.
	 * @throws IllegalArgumentException If {@code amount < 0}.
	 */
	public void setDamage(DDDDamageType type, float amount) {
		if(amount < 0) {
			throw new IllegalArgumentException("Can't inflict a negative amount of damage!");
		}
		this.map.put(type, amount);
	}
}
