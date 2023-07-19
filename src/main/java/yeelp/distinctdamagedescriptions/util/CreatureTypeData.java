package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.potion.Potion;

/**
 * Data storage for CreatureTypes
 * 
 * @author Yeelp
 *
 */
public final class CreatureTypeData {
	/**
	 * Default CreatureTypeData. Falls back to this if no CreatureTypeData applies.
	 * Has no special properties.
	 */
	public static final CreatureTypeData UNKNOWN = new CreatureTypeData();

	private String type;
	private Set<String> potionImmunities;
	public boolean criticalImmunity;

	/**
	 * Build a new CreatureTypeData
	 * 
	 * @param name
	 * @param potionImmunities set of potion effect strings this creature type is
	 *                         immune to.
	 * @param criticalImmunity if this creature type is immune to criticals.
	 */
	public CreatureTypeData(String name, Set<String> potionImmunities, boolean criticalImmunity) {
		this.type = name;
		this.potionImmunities = potionImmunities;
		this.criticalImmunity = criticalImmunity;
	}

	private CreatureTypeData() {
		this.type = "unknown";
		this.potionImmunities = new HashSet<String>();
		this.criticalImmunity = false;
	}

	/**
	 * Get the type name
	 * 
	 * @return type name
	 */
	public String getTypeName() {
		return this.type;
	}

	/**
	 * Get a set of potion immunities
	 * 
	 * @return the set of potions this creature type is immune to.
	 */
	public Set<String> getPotionImmunities() {
		return this.potionImmunities;
	}
	
	/**
	 * Add a potion immunity to this creature type
	 * @param potion Potion immunity to add.
	 */
	public void addPotionImmunity(Potion potion) {
		this.potionImmunities.add(potion.getRegistryName().toString());
	}
	
	public void removePotionImmunity(Potion potion) {
		this.potionImmunities.remove(potion.getRegistryName().toString());
	}

	/**
	 * Is this CreatureTypeData providing crit immunity?
	 * 
	 * @return true if immune, false if not
	 */
	public boolean isImmuneToCriticals() {
		return this.criticalImmunity;
	}

	@Override
	public String toString() {
		return String.format("%s (Critical Hit Immunity: %s, Potion Immunities: %s)", this.type, this.criticalImmunity, this.potionImmunities);
	}
}
