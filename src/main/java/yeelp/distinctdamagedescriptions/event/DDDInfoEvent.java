package yeelp.distinctdamagedescriptions.event;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageResistances;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DamageMap;

/**
 * Base event for all info DDD gathers to let other mods and features hook into
 * this to alter information during damage calculations. <br>
 * Some of these events are {@link Cancelable}. <br>
 * None of these events have a result {@link HasResult}. <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}.
 * 
 * @author Yeelp
 *
 */
public abstract class DDDInfoEvent extends Event {

	private EntityLivingBase entity;

	public DDDInfoEvent(EntityLivingBase entity) {
		super();
		this.entity = entity;
	}

	/**
	 * @return the entity
	 */
	public EntityLivingBase getEntity() {
		return this.entity;
	}

	/**
	 * Fired when DDD gathers immunities during damage calculations. You can add or
	 * remove immunities here. <br>
	 * These immunities do not affect the entity permanently. This is for context
	 * based immunities. <br>
	 * To give an immunity to a target permanently, or to remove an immunity from a
	 * target permanently, regardless of the context in which is was granted, use
	 * {@link IDamageResistances#setImmunity}. <br>
	 * This event is not {@link Cancelable}. <br>
	 * This event does not have a result {@link HasResult}.
	 * 
	 * @author Yeelp
	 *
	 */
	public static final class GatherImmunities extends DDDInfoEvent {

		/**
		 * The set of immunities DDD has gathered thus far, including those from
		 * {@link IMobResistances} immunities.
		 */
		public final Set<DDDDamageType> immunities;

		public GatherImmunities(EntityLivingBase entity, Set<DDDDamageType> immunities) {
			super(entity);
			this.immunities = new HashSet<DDDDamageType>(immunities);
		}
	}

	/**
	 * Fired before DDD updates a mob's adaptive resistances. Changing this mob's
	 * adaptability status will not prevent the resistances from being updated.
	 * Instead, cancel this event instead. <br>
	 * 
	 * This event is {@link Cancelable}. <br>
	 * When cancelled, this mob's adaptive resistances aren't updated. Instead, they keep their current adaptive resistances 
	 * @author Yeelp
	 *
	 */
	@Cancelable
	public static final class UpdateAdaptiveResistances extends DDDInfoEvent {

		/**
		 * The current set of types this mob is adapting to.
		 */
		public final Set<DDDDamageType> currentlyAdaptiveTo;

		/**
		 * The set of damage types inflicted.
		 */
		public final Set<DDDDamageType> damageTypesInflicted;

		private final DamageMap dmg;

		public UpdateAdaptiveResistances(EntityLivingBase entity, DamageMap dmg) {
			super(entity);
			IMobResistances resists = DDDAPI.accessor.getMobResistances(entity);
			this.dmg = dmg;
			this.damageTypesInflicted = new HashSet<DDDDamageType>(this.dmg.keySet());
			this.currentlyAdaptiveTo = DDDRegistries.damageTypes.getAll().stream().filter(resists::isAdaptiveTo).collect(Collectors.toSet());
		}
		
		/**
		 * Get the damage inflicted of a certain type.
		 * @param type
		 * @return the damage inflicted of that type.
		 */
		public float getDamage(DDDDamageType type) {
			return this.dmg.get(type);
		}
	}
}
