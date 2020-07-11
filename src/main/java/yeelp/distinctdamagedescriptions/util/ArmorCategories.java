package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;
import java.util.Set;

public final class ArmorCategories
{
	private ComparableTriple<Float, Float, Float> armor, toughness;
	/**
	 * Create a new base armor
	 * @param slashing slashing armor.
	 * @param piercing piercing armor.
	 * @param bludgeoning bludgeoning armor.
	 * @param slashTough slashing toughness.
	 * @param pierceTough piercing toughness.
	 * @param bludgeTough bludgeoning toughness.
	 */
	public ArmorCategories(float slashing, float piercing, float bludgeoning, float slashTough, float pierceTough, float bludgeTough)
	{
		this.armor = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
		this.toughness = new ComparableTriple<Float, Float, Float>(slashTough, pierceTough, bludgeTough);
	}
	
	/**
	 * Build an ArmorCategories from two triples.
	 * @param armor
	 * @param toughness
	 */
	public ArmorCategories(ComparableTriple<Float, Float, Float> armor, ComparableTriple<Float, Float, Float> toughness)
	{
		this.armor = armor;
		this.toughness = toughness;
	}
	
	/**
	 * Get slashing armor
	 * @return slashing armor
	 */
	public float getSlashingArmor()
	{
		return this.armor.getLeft();
	}
	
	/**
	 * Get piercing armor
	 * @return piercing armor
	 */
	public float getPiercingArmor()
	{
		return this.armor.getMiddle();
	}
	
	/**
	 * Get bludgeoning armor
	 * @return bludgeoning armor
	 */
	public float getBludgeoningArmor()
	{
		return this.armor.getRight();
	}
	
	/**
	 * Get slashing toughness
	 * @return slashing toughness
	 */
	public float getSlashingToughness()
	{
		return this.toughness.getLeft();
	}
	
	/**
	 * Get piercing toughness
	 * @return piercing toughness
	 */
	public float getPiercingToughness()
	{
		return this.toughness.getMiddle();
	}
	
	/**
	 * Get bludgeoning toughness
	 * @return bludgeoning toughness
	 */
	public float getBludgeoningToughness()
	{
		return this.toughness.getRight();
	}
	
	/**
	 * Get the DamageTypes blocked by a collection of ArmorCategories
	 * @param armorCategories
	 * @return Set of DamageType enums.
	 */
	public static Set<DamageType> getDamageTypes(ArmorCategories...armorCategories)
	{
		HashSet<DamageType> set = new HashSet<DamageType>();
		for(ArmorCategories cat : armorCategories)
		{
			if(cat.getSlashingArmor() > 0)
			{
				set.add(DamageType.SLASHING);
			}
			if(cat.getPiercingArmor() > 0)
			{
				set.add(DamageType.PIERCING);
			}
			if(cat.getBludgeoningArmor() > 0)
			{
				set.add(DamageType.BLUDGEONING);
			}
			if(set.size() == DamageType.values().length)
			{
				return set;
			}
		}
		return set;
	}
	
	@Override
	public String toString()
	{
		return String.format("(slashing: %f, piercing: %f, bludgeoning: %f)", getSlashingArmor(), getPiercingArmor(), getBludgeoningArmor());
	}
}
