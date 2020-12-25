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
	private Set<String> potionImmunities;
	private boolean criticalImmunity;
	
	/**
	 * Build a new CreatureTypeData
	 * @param name
	 * @param potionImmunities set of potion effect strings this creature type is immune to.
	 * @param criticalImmunity if this creature type is immune to criticals.
	 */
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
	
	/**
	 * Get a set of potion immunities
	 * @return the set of potions this creature type is immune to.
	 */
	public Set<String> getPotionImmunities()
	{
		return this.potionImmunities;
	}
	
	
	/**
	 * Is this CreatureTypeData providing crit immunity?
	 * @return true if immune, false if not
	 */
	public boolean isImmuneToCriticals()
	{
		return criticalImmunity;
	}
}
