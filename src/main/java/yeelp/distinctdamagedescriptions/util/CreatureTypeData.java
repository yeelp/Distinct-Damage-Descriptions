package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;

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
	private MobResistanceCategories resists;
	private HashSet<String> potionImmunities;
	private boolean criticalImmunity;
	/**
	 * Make a new CreatureTypeData
	 * @param name name of this data
	 * @param resistances resistances this data has
	 * @param potionImmunities potion immunities this data has
	 * @param criticalImmunity if this data has crit immunity.
	 */
	public CreatureTypeData(String name, MobResistanceCategories resistances, HashSet<String> potionImmunities, boolean criticalImmunity)
	{
		this.type = name;
		this.resists = resistances;
		this.potionImmunities = potionImmunities;
		this.criticalImmunity = criticalImmunity;
	}
	
	private CreatureTypeData()
	{
		type = "unknown";
		resists = new MobResistanceCategories(0, 0, 0, false, false, false, 0, 0);
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
	 * Get the resistances
	 * @return resistances
	 */
	public MobResistanceCategories getMobResistances()
	{
		return resists;
	}
	
	/**
	 * Is this CreatureTypeData providing immunity to this PotionEffect
	 * @param effect effect in question
	 * @return true if immune, false if not
	 */
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
}
