package yeelp.distinctdamagedescriptions.event;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * Base class for all DamageDescriptionEvent events.
 * <br>
 * Some events are {@link Cancelable}. When canceled, damage is unaltered, and DDD's calculations are essentially ignored. This does not prevent damage.
 * <br>
 * These events do not have a result {@link HasResult}
 * <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}
 * @author Yeelp
 *
 */
public abstract class DamageDescriptionEvent extends Event
{
	private final Entity attacker;
	private final EntityLivingBase defender;
	private final NonNullMap<String, Float> damage, resistances;
	private final NonNullMap<String, Tuple<Float, Float>> armorMap;
	/**
	 * Create a new DamageDescriptionEvent
	 * @param attacker the attacking Entity. May be null.
	 * @param defender the defending EntityLivingBase.
	 * @param damage a Map outlining the distribution of damage for applicable types.
	 * @param resistances a Map outlining the distribution of resistances for applicable types.
	 * @param armor a Map that maps damage type strings to a tuple (armor, toughness) for that damage type.
	 */
	public DamageDescriptionEvent(Entity attacker, EntityLivingBase defender, Map<String, Float> damage, Map<String, Float> resistances, Map<String, Tuple<Float, Float>> armorMap)
	{
		super();
		this.damage = new NonNullMap<String, Float>(0.0f); 
		this.resistances = new NonNullMap<String, Float>(0.0f);
		this.armorMap = new NonNullMap<String, Tuple<Float, Float>>(new Tuple<Float, Float>(0.0f, 0.0f));
		this.damage.putAll(damage);
		this.resistances.putAll(resistances);
		this.armorMap.putAll(armorMap);
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
	 * Get damage of a particular type
	 * @param type
	 * @return the damage of that type
	 */
	public float getDamage(String type)
	{
		return this.damage.get(type);
	}
	
	/**
	 * Set damage for a particular type
	 * @param type
	 * @param amount amount to set.
	 */
	public void setDamage(String type, float amount)
	{
		this.damage.put(type, amount);
	}
	
	/**
	 * Get the resistance for a damage type.
	 * @param type the type of damage
	 * @return the resistance
	 */
	public float getResistance(String type)
	{
		return this.resistances.get(type);
	}
	
	/**
	 * Set the resistance for this damage type.
	 * @param type the type of damage
	 * @param newResistance
	 */
	public void setResistance(String type, float newResistance)
	{
		this.resistances.put(type, newResistance);
	}
	
	/**
	 * Get armor and toughness values for a certain damage type.
	 * @param type
	 * @return a Tuple (armor, toughness)
	 */
	public Tuple<Float, Float> getArmorAndToughness(String type)
	{
		return this.armorMap.get(type);
	}
	
	/**
	 * Set armor and toughness values for a particular type.
	 * @param type
	 * @param armor armor to set
	 * @param toughness toughness to set.
	 */
	public void setArmorAndToughness(String type, float armor, float toughness)
	{
		this.armorMap.put(type, new Tuple<Float, Float>(armor, toughness));
	}
	
	/**
	 * Get a map of all damage types.
	 * @return the internal damage map
	 */
	public Map<String, Float> getAllDamages()
	{
		return this.damage;
	}
	
	/**
	 * Get a map of all resistances
	 * @return the internal resistance map
	 */
	public Map<String, Float> getAllResistances()
	{
		return this.resistances;
	}
	
	/**
	 * Get a map of all armor and toughness values
	 * @return the internal armor map.
	 */
	public Map<String, Tuple<Float, Float>> getAllArmor()
	{
		return this.armorMap;
	}
	/**
	 * Event that fires <strong>BEFORE</strong> DDD's damage calculations. Resistances and damage can be altered to factor in DDD's damage calculations.
	 * <br>
	 * This event is {@link Cancelable}. When canceled, DDD skips doing damage calculations.
	 * @author Yeelp
	 *
	 */
	@Cancelable
	public static final class Pre extends DamageDescriptionEvent
	{
		public Pre(Entity attacker, EntityLivingBase defender, Map<String, Float> damage, Map<String, Float> resistances, Map<String, Tuple<Float, Float>> armorMap)
		{
			super(attacker, defender, damage, resistances, armorMap);
		}
	}
	
	/**
	 * Event that fires <strong>AFTER</strong> DDD's damage calculations. Damage can be altered to ignore DDD's damage calculations.
	 * Note that changing mob resistances or armor values at this point does nothing. They are only provided for ease of access, and to see how they were
	 * altered during damage calculations. DDD ignores these values, and only retrieves and uses the damage map after the event is fired. 
	 * <br>
	 * This event is not {@link Cancelable}.
	 * @author Yeelp
	 *
	 */
	public static final class Post extends DamageDescriptionEvent
	{
		public Post(Entity attacker, EntityLivingBase defender, Map<String, Float> damage, Map<String, Float> resistances, Map<String, Tuple<Float, Float>> armorMap)
		{
			super(attacker, defender, damage, resistances, armorMap);
		}
	}
}
