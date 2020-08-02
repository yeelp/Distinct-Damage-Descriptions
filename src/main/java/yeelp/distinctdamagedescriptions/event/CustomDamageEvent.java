package yeelp.distinctdamagedescriptions.event;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;

/**
 * Event for custom damage types. Need both creature types <strong>AND</strong> custom damage types enabled for this event to fire.
 * This event is fired after all physical damage calculations, if any, are complete.
 * This event contains the context of the damage types inflicted, allowing for custom behaviour.
 * It also contains the <strong>total</strong> amount of damage inflicted and the <strong>total</strong> percent of damage that will be reduced, allowing for control over these values.
 * <br>
 * This event is not {@link Cancelable}. <br>
 * <br>
 * This event does not have a result {@link HasResult}.
 * @author Yeelp
 *
 */
public final class CustomDamageEvent extends DamageDescriptionEvent
{
	private final HashSet<String> damageTypes;
	private float resistance;
	/**
	 * Build a new CustomeDamageEvent
	 * @param attacker
	 * @param defender
	 * @param amount
	 * @param resistance
	 * @param damageSources
	 */
	public CustomDamageEvent(@Nullable Entity attacker, @Nonnull EntityLivingBase defender, float amount, float resistance, String...damageSources)
	{
		super(attacker, defender, amount);
		this.damageTypes = new HashSet<String>(Arrays.asList(damageSources));
		this.resistance = resistance;
	}
	
	/**
	 * Get all the damage types inflicted by this attack. Typically will be around one, but almost certainly never more than three.
	 * @return
	 */
	public String[] getDamageTypes()
	{
		return damageTypes.toArray(new String[] {});
	}
	
	/**
	 * Is the specified damage type part of the set of damage types inflicted?
	 * @param damageTypeName the name of the damage type. Note custom damage types are prefixed with "ddd_", but vanilla or modded damage source strings may be checked for too.
	 * @return true if present, false if not.
	 */
	public boolean hasDamageType(String damageTypeName)
	{
		return damageTypes.contains(damageTypeName);
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
