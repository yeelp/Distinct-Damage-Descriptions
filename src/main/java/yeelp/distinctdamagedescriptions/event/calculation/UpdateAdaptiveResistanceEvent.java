package yeelp.distinctdamagedescriptions.event.calculation;

import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;
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

	public UpdateAdaptiveResistanceEvent(@Nullable Entity attacker, @Nullable Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap dmg, ResistMap resists, Set<DDDDamageType> immunities) {
		super(attacker, trueAttacker, defender, src, (DamageMap) dmg.clone());
		this.immunities = immunities;
		this.resists = resists;
	}

	public float getResistance(DDDDamageType type) {
		return this.resists.get(type);
	}

	public boolean hasImmunity(DDDDamageType type) {
		return this.immunities.contains(type);
	}

	public void ignoreType(DDDDamageType type) {
		this.dmg.remove(type);
	}
}
