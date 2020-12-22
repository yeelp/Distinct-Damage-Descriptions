package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple container for storing resistances
 * @author Yeelp
 *
 */
public abstract class ResistanceCategories
{
	private Map<String, Float> resistMap;
	private Set<String> dmgImmunities;
	
	public ResistanceCategories(Map<String, Float> resistances, Collection<String> immunities)
	{
		this.resistMap = resistances;
		this.dmgImmunities = new HashSet<String>(immunities);
	}
	
	/**
	 * Get resistance for a certain damage type.
	 * @param type
	 * @return resistance to that damage type.
	 */
	public float getResistance(String type)
	{
		return this.resistMap.get(type);
	}
	
	/**
	 * Check if immunity to this damage type exists
	 * @param type
	 * @return true if immune, false if not.
	 */
	public boolean hasImmunity(String type)
	{
		return this.dmgImmunities.contains(type);
	}
	
	/**
	 * Get the map of resistances
	 * @return the map of resistances
	 */
	public Map<String, Float> getResistanceMap()
	{
		return this.resistMap;
	}
	
	/**
	 * get the set of immunities
	 * @return the set of immunities
	 */
	public Set<String> getImmunities()
	{
		return this.dmgImmunities;
	}
}
