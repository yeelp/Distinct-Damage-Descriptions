package yeelp.distinctdamagedescriptions.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.util.DamageType;

/**
 * Base class for all DamageDescriptionEvent events.
 * <br>
 * These events are not {@link Cancelable}
 * <br>
 * These events do not have a result {@link HasResult}
 * <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 *
 */
public abstract class DamageDescriptionEvent extends Event
{
	private float amount;
	private final Entity attacker;
	private final EntityLivingBase defender;
	/**
	 * Create a new DamageDescriptionEvent
	 * @param attacker the attacking Entity. May be null.
	 * @param defender the defending EntityLivingBase.
	 * @param amount the amount of damage of {@code type} damage being inflicted.
	 */
	public DamageDescriptionEvent(Entity attacker, EntityLivingBase defender, float amount)
	{
		super();
		this.amount = amount;
		this.attacker = attacker;
		this.defender = defender;
	}
	
	/**
	 * Get the Entity inflicting the damage. The arrow, not the shooter.
	 * @return The attacking Entity
	 */
	@Nullable
	public Entity getAttacker()
	{
		return attacker;
	}
	
	/**
	 * Get the defending EntityLivingBase.
	 * @return The defending EntityLivingBase
	 */
	@Nonnull
	public EntityLivingBase getDefender()
	{
		return defender;
	}
	
	/**
	 * Get the amount of damage inflicted.
	 * @return the amount of damage.
	 */
	public float getDamage()
	{
		return this.amount;
	}
	
	/**
	 * Set the amount of damage of this type that will be inflicted.
	 * @param amount amount to set.
	 */
	public void setDamage(float amount)
	{
		this.amount = amount;
	}
	
	/**
	 * Get the resistance for this damage type.
	 * @return the resistance
	 */
	public abstract float getResistance();
	
	/**
	 * Set the resistance for this damage type.
	 * @param newResistance
	 */
	public abstract void setResistance(float newResistance);
}
