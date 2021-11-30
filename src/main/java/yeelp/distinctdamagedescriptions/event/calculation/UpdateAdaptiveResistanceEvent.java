package yeelp.distinctdamagedescriptions.event.calculation;

import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.ResistMap;

/**
 * This event fires every calculation to determine if a mob's resistances should
 * be updated by adaptability. This event even fires if the mob is question
 * doesn't have adaptability. <br>
 * <br>
 * The event contains the resistances and immunities used during that
 * calculation. They can't be altered, as changing them, would have no
 * effect.<br>
 * <br>
 * The event also includes a way to selectively remove certain types from
 * triggering adaptability. The event stores a clone of the damage map
 * inflicted, and calling {@link #ignoreType(DDDDamageType)} can be used to
 * prevent the mob's resistance of that type from changing. <br>
 * <br>
 * This event is not {@link Cancelable}. <br>
 * This event has a {@link Result} {@link HasResult}: <br>
 * - {@link Result#ALLOW}: Will forcefully update a mob's resistance, regardless
 * if they actually have adaptability. <br>
 * - {@link Result#DEFAULT}: Default behaviour. That is, will only update
 * adaptive resistance if the mob is actually adaptive. <br>
 * - {@link Result#DENY}: Will skip updating a mob's resistance, effectively
 * preventing their adaptability (if they have it) from kicking in.
 * 
 * @author Yeelp
 *
 */
@HasResult
public final class UpdateAdaptiveResistanceEvent extends DDDCalculationEvent {

	private final ResistMap resists;
	private final Set<DDDDamageType> immunities;
	private float adaptiveAmount;

	public UpdateAdaptiveResistanceEvent(@Nullable Entity attacker, @Nullable Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap dmg, ResistMap resists, Set<DDDDamageType> immunities) {
		super(attacker, trueAttacker, defender, src, (DamageMap) dmg.clone());
		this.immunities = immunities;
		this.resists = resists;
		this.adaptiveAmount = DDDAPI.accessor.getMobResistances(defender).get().getAdaptiveAmount();
	}

	/**
	 * Get this entity's current resistance of a certain type. Does not reflect
	 * their actual resistance, but the resistance value used in calculations.
	 * 
	 * @param type
	 * @return That entity's resistance to that type.
	 */
	public float getResistance(DDDDamageType type) {
		return this.resists.get(type);
	}

	/**
	 * Is this entity currently immune to this type? Does not reflect their regular
	 * immunities, just immunities used in this calculation.
	 * 
	 * @param type
	 * @return true if immune
	 */
	public boolean hasImmunity(DDDDamageType type) {
		return this.immunities.contains(type);
	}

	/**
	 * Prevent this entity from adapting to this type, this time only.
	 * 
	 * @param type
	 */
	public void ignoreType(DDDDamageType type) {
		this.dmg.remove(type);
	}

	/**
	 * Get the current map of damage this entity is adapting to. This will be used
	 * when updating adpative resistance, but <strong>NOT</strong> used as the
	 * damage inflicted. In reality, this damage map is a copy; changes to the
	 * damage values in this map do <strong>NOT</strong> change the amount of damage
	 * inflicted. It will however, change how adpatability changes with adaptive
	 * weakness enabled!
	 * 
	 * @return The damage being adapted to
	 */
	public DamageMap getDamageToAdaptTo() {
		return this.dmg;
	}
	
	/**
	 * Get the adaptive amount this entity will be using.
	 * @return the adaptive amount
	 */
	public float getAdaptiveAmount() {
		return this.adaptiveAmount;
	}
	
	/**
	 * Set the adaptive amount for this entity. This amount will only be used for this calculation, then set back to whatever the amount was before
	 * @param amount
	 */
	public void setAdaptiveAmount(float amount) {
		this.adaptiveAmount = amount;
	}
}
