package yeelp.distinctdamagedescriptions.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;

/**
 * Event that DDD fires when it assigns mob resistances to allow control over
 * resistances. The event fires during an {@link EntityJoinWorldEvent}.
 * Modifying the resistances here, will modify the entity's resistances
 * permanently. <br>
 * This event is {@link Cancelable}. <br>
 * When cancelled, empty mob resistances are assigned. No resistances, weakness, immunities or adaptability. <br>
 * This event does not have a result. {@link HasResult}
 * 
 * @author Yeelp
 *
 */
@Cancelable
public class AssignMobResistancesEvent extends Event {

	private final EntityLivingBase entity;
	private final World world;
	private final IMobResistances resists;

	public AssignMobResistancesEvent(EntityLivingBase entity, World world, IMobResistances resistances) {
		this.entity = entity;
		this.world = world;
		this.resists = resistances;
	}

	/**
	 * Get the mob resistances
	 * @return The mob resistances capability
	 */
	public IMobResistances getResistances() {
		return this.resists;
	}

	/**
	 * Get the living entity being assigned these resistances
	 * @return The entity being assigned.
	 */
	public EntityLivingBase getEntityLivingBase() {
		return this.entity;
	}

	/**
	 * Get the world
	 * @return the world.
	 */
	public World getWorld() {
		return this.world;
	}
}
