package yeelp.distinctdamagedescriptions.event;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import yeelp.distinctdamagedescriptions.util.DamageType;

/**
 * Base class for all DamageDescriptionEvent events.
 * <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 *
 */
public abstract class DamageDescriptionEvent extends Event
{
	private final DamageType type;
	private final LivingAttackEvent evt;
	private float amount;
	/**
	 * Create a new DamageDescriptionEvent
	 * @param type the DamageType of this event
	 * @param evt the underlying LivingAttackEvent
	 * @param amount the amount of damage of {@code type} damage being inflicted.
	 */
	public DamageDescriptionEvent(DamageType type, LivingAttackEvent evt, float amount)
	{
		super();
		this.evt = evt;
		this.type = type;
		this.amount = amount;
	}
	
	/**
	 * Get the DamageType for this event
	 * @return the DamageType enum.
	 */
	public DamageType getType()
	{
		return this.type;
	}
	
	/**
	 * Get the underlying LivingAttackEvent that fired this DamageDescriptionEvent
	 * @return the underlying LivingAttackEvent.
	 */
	public LivingAttackEvent getLivingAttackEvent()
	{
		return this.evt;
	}
	
	/**
	 * Get the amount of damage inflicted.
	 * @return the amount of damage.
	 */
	public float getAmount()
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
	 * This override cancels this event, which also cancels the underlying LivingAttackEvent, thus preventing damage. <br>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public void setCanceled(boolean cancel)
	{
		evt.setCanceled(cancel);
		super.setCanceled(cancel);
	}
	
	/**
	 * Fired as soon as possible during a LivingAttackEvent, if the attacking weapon inflicts a non-zero amount of slashing damage, to allow control over its value.
	 * <br>
	 * This event is {@link Cancelable}. When canceled, the underlying LivingAttackEvent is also canceled, thus preventing damage. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class SlashingDamage extends DamageDescriptionEvent
	{
		public SlashingDamage(LivingAttackEvent evt, float amount)
		{
			super(DamageType.SLASHING, evt, amount);
		}
	}
	
	/**
	 * Fired as soon as possible during a LivingAttackEvent, if the attacking weapon inflicts a non-zero amount of bludgeoning damage, to allow control over its value.
	 * <br>
	 * This event is {@link Cancelable}. When canceled, the underlying LivingAttackEvent is also canceled, thus preventing damage. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class BludgeoningDamage extends DamageDescriptionEvent
	{
		public BludgeoningDamage(LivingAttackEvent evt, float amount)
		{
			super(DamageType.BLUDGEONING, evt, amount);
		}
	}
	
	/**
	 * Fired as soon as possible during a LivingAttackEvent, if the attacking weapon inflicts a non-zero amount of piercing damage, to allow control over its value.
	 * <br>
	 * This event is {@link Cancelable}. When canceled, the underlying LivingAttackEvent is also canceled, thus preventing damage. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class PiercingDamage extends DamageDescriptionEvent
	{
		public PiercingDamage(LivingAttackEvent evt, float amount)
		{
			super(DamageType.PIERCING, evt, amount);
		}
	}
}
