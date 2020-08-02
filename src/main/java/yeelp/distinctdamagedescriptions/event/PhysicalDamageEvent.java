package yeelp.distinctdamagedescriptions.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;
import yeelp.distinctdamagedescriptions.util.DamageType;

/**
 * Base class for all physical damage events - slashing piercing and bludgeoning
 * <br>
 * These events are not {@link Cancelable}
 * <br>
 * These events do not have a result {@link HasResult}
 * <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 * 
 */
public abstract class PhysicalDamageEvent extends DamageDescriptionEvent
{
	private final DamageType type;
	private float resistance;
	public PhysicalDamageEvent(DamageType type, float amount, float resistance, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		super(attacker, defender, amount);
		this.type = type;
		this.resistance = resistance;
	}
	
	public DamageType getDamageType()
	{
		return type;
	}
	
	@Override
	public float getResistance()
	{
		return resistance;
	}
	
	@Override
	public void setResistance(float newResistance)
	{
		resistance = newResistance;
	}
	
	/**
	 * Fired as soon as possible during a LivingHurtEvent, if the attacking weapon inflicts a non-zero amount of slashing damage, to allow control over its value.
	 * <br>
	 * This event is not {@link Cancelable}. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class SlashingDamage extends PhysicalDamageEvent
	{
		public SlashingDamage(float amount, float resistance, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
		{
			super(DamageType.SLASHING, amount, resistance, attacker, defender);
		}	
	}
	
	/**
	 * Fired as soon as possible during a LivingHurtEvent, if the attacking weapon inflicts a non-zero amount of bludgeoning damage, to allow control over its value.
	 * <br>
	 * This event is not {@link Cancelable}. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class BludgeoningDamage extends PhysicalDamageEvent
	{
		public BludgeoningDamage(float amount, float resistance, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
		{
			super(DamageType.BLUDGEONING, amount, resistance, attacker, defender);
		}
	}
	
	/**
	 * Fired as soon as possible during a LivingHurtEvent, if the attacking weapon inflicts a non-zero amount of piercing damage, to allow control over its value.
	 * <br>
	 * This event is not {@link Cancelable}. <br>
	 * <br>
	 * This event does not have a result {@link HasResult}
	 */
	public static class PiercingDamage extends PhysicalDamageEvent
	{
		public PiercingDamage(float amount, float resistance, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
		{
			super(DamageType.PIERCING, amount, resistance, attacker, defender);
		}
	}
}
