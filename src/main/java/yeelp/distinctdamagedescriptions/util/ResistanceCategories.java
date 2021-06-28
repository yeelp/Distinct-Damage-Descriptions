package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;

/**
 * A simple container for storing resistances
 * 
 * @author Yeelp
 *
 */
public abstract class ResistanceCategories {
	private Map<DDDDamageType, Float> resistMap;
	private Set<DDDDamageType> dmgImmunities;

	public ResistanceCategories(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities) {
		this.resistMap = resistances;
		this.dmgImmunities = new HashSet<DDDDamageType>(immunities);
	}

	/**
	 * Get resistance for a certain damage type.
	 * 
	 * @param type
	 * @return resistance to that damage type.
	 */
	public float getResistance(DDDDamageType type) {
		return this.resistMap.get(type);
	}

	/**
	 * Check if immunity to this damage type exists
	 * 
	 * @param type
	 * @return true if immune, false if not.
	 */
	public boolean hasImmunity(DDDDamageType type) {
		return this.dmgImmunities.contains(type);
	}

	/**
	 * Get the map of resistances
	 * 
	 * @return the map of resistances
	 */
	public Map<DDDDamageType, Float> getResistanceMap() {
		return this.resistMap;
	}

	/**
	 * get the set of immunities
	 * 
	 * @return the set of immunities
	 */
	public Set<DDDDamageType> getImmunities() {
		return this.dmgImmunities;
	}
}
