package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.potion.PotionEffect;

/**
 * Data storage for CreatureTypes
 * @author Yeelp
 *
 */
public final class CreatureTypeData
{
	/**
	 * Default CreatureTypeData. Falls back to this if no CreatureTypeData applies. Has no special properties.
	 */
	public static final CreatureTypeData UNKNOWN = new CreatureTypeData();
	
	private String type;
	@Deprecated
	private MobResistanceCategories resists;
	private Set<String> potionImmunities;
	@Deprecated
	private Map<String, Float> extraResistances;
	private boolean criticalImmunity;
	/**
	 * Make a new CreatureTypeData
	 * @param name name of this data
	 * @param resistances resistances this data has
	 * @param potionImmunities potion immunities this data has
	 * @param criticalImmunity if this data has crit immunity.
	 * @param extraResistance additional resistances. A Map: String -> Float that indicates additional modifiers for all damage types.
	 */
	@Deprecated
	public CreatureTypeData(String name, MobResistanceCategories resistances, HashSet<String> potionImmunities, boolean criticalImmunity, Map<String, Float> extraResistances)
	{
		this.type = name;
		this.resists = resistances;
		this.potionImmunities = potionImmunities;
		this.criticalImmunity = criticalImmunity;
		this.extraResistances = extraResistances;
	}
	
	public CreatureTypeData(String name, Set<String> potionImmunities, boolean criticalImmunity)
	{
		this.type = name;
		this.potionImmunities = potionImmunities;
		this.criticalImmunity = criticalImmunity;
	}
	
	private CreatureTypeData()
	{
		type = "unknown";
		potionImmunities = new HashSet<String>();
		criticalImmunity = false;
	}
	
	/**
	 * Get the type name
	 * @return type name
	 */
	public String getTypeName()
	{
		return type;
	}
	
	public Set<String> getPotionImmunities()
	{
		return this.potionImmunities;
	}
	
	/**
	 * Get the resistances
	 * @return resistances
	 */
	@Deprecated
	public MobResistanceCategories getMobResistances()
	{
		return resists;
	}
	
	/**
	 * Is this CreatureTypeData providing immunity to this PotionEffect
	 * @param effect effect in question
	 * @return true if immune, false if not
	 */
	@Deprecated
	public boolean isImmuneToPotionEffect(PotionEffect effect)
	{
		return potionImmunities.contains(effect.getPotion().getRegistryName().toString());
	}
	
	/**
	 * Is this CreatureTypeData providing crit immunity?
	 * @return true if immune, false if not
	 */
	public boolean isImmuneToCriticals()
	{
		return criticalImmunity;
	}
	
	/**
	 * Get the modifier for extra damage types.
	 * @param damageTypeName name of the damage type.
	 * @return a float modifier for damage.
	 */
	@Deprecated
	public float getModifierForDamageType(String damageTypeName)
	{
		return extraResistances.get(damageTypeName);
	}
}
