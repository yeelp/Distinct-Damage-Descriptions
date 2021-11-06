package yeelp.distinctdamagedescriptions.event.classification;

import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.ResistMap;

/**
 * This event is fired when DDD gathers creature defenses (resistances and immunities).
 * <br>
 * This event is not {@link Cancelable}.
 * <br>
 * This event does not have a result {@link HasResult}.
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 *
 */
public final class GatherDefensesEvent extends DDDClassificationEvent {

	private final ResistMap map;
	private final Set<DDDDamageType> immunities;
	/**
	 * Construct a new event
	 * @param attacker The attacking entity
	 * @param trueAttacker The true attacking entity e.g. The shooter of the projectile
	 * @param defender The defending entity
	 * @param map the map of resistances DDD found from capabilities
	 * @param immunities the set of immunities DDD found from capabilities
	 */
	public GatherDefensesEvent(@Nullable Entity attacker, @Nullable Entity trueAttacker, EntityLivingBase defender, DamageSource src, ResistMap map, Set<DDDDamageType> immunities) {
		super(attacker, trueAttacker, defender, src);
		this.map = map;
		this.immunities = immunities;
	}

	/**
	 * Get resistance
	 * @param type
	 * @return the defender's current resistance to that type.
	 */
	public float getResistance(DDDDamageType type) {
		return this.map.get(type);
	}
	
	/**
	 * Is the defender currently immune to this damage type?
	 * @param type
	 * @return true if immune, false if not.
	 */
	public boolean hasImmunity(DDDDamageType type) {
		return this.immunities.contains(type);
	}
	
	/**
	 * Does the defending entity have a resistance against this damage type?
	 * @param type
	 * @return true if the defending entity's resistance map includes this type and is larger than 0.
	 */
	public boolean hasResistance(DDDDamageType type) {
		return this.map.get(type) > 0;
	}
	
	/**
	 * Set the defender's resistance for this type. Only applies to this damage calculation. Can set weaknesses with this too.
	 * @param type
	 * @param amount
	 */
	public void setResistance(DDDDamageType type, float amount) {
		this.map.put(type, amount);
	}
	
	/**
	 * Add {@code type} as a type the defender is immune to for this calculation
	 * @param type
	 */
	public void addImmunity(DDDDamageType type) {
		this.immunities.add(type);
	}
	
	/**
	 * Clear all immunities
	 */
	public void clearImmunities() {
		this.immunities.clear();
	}
	
	/**
	 * Clear all resistances
	 */
	public void clearResistances() {
		this.map.clear();
	}
}
