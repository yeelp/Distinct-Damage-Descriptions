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
 * This event is not {@link Cancelable}
 * <br>
 * This event does not have a result {@link HasResult}
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 * 
 */
public class PhysicalDamageEvent extends DamageDescriptionEvent
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
}
