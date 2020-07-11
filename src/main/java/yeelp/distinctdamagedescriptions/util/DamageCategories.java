package yeelp.distinctdamagedescriptions.util;

import java.util.HashSet;
import java.util.Set;

public final class DamageCategories
{
	private ComparableTriple<Float, Float, Float> damage;
	/**
	 * Create a new base damage
	 * @param slashing slashing damage.
	 * @param piercing piercing damage.
	 * @param bludgeoning bludgeoning damage.
	 */
	public DamageCategories(float slashing, float piercing, float bludgeoning)
	{
		this.damage = new ComparableTriple<Float, Float, Float>(slashing, piercing, bludgeoning);
	}
	
	/**
	 * Create a new DamageCategories from a triple
	 * @param triple
	 */
	public DamageCategories(ComparableTriple<Float, Float, Float> triple)
	{
		this.damage = triple;
	}
	
	/**
	 * Get slashing damage
	 * @return slashing damage
	 */
	public float getSlashingDamage()
	{
		return this.damage.getLeft();
	}
	
	/**
	 * Get piercing damage
	 * @return piercing damage
	 */
	public float getPiercingDamage()
	{
		return this.damage.getMiddle();
	}
	
	/**
	 * Get bludgeoning damage
	 * @return bludgeoning damage
	 */
	public float getBludgeoningDamage()
	{
		return this.damage.getRight();
	}
	
	/**
	 * Get the DamageTypes inflicted by a collection of DamageCategories
	 * @param damageCategories
	 * @return Set of DamageType enums.
	 */
	public static Set<DamageType> getDamageTypes(DamageCategories...damageCategories)
	{
		HashSet<DamageType> set = new HashSet<DamageType>();
		for(DamageCategories cat : damageCategories)
		{
			if(cat.getSlashingDamage() > 0)
			{
				set.add(DamageType.SLASHING);
			}
			if(cat.getPiercingDamage() > 0)
			{
				set.add(DamageType.PIERCING);
			}
			if(cat.getBludgeoningDamage() > 0)
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
		return String.format("(slashing: %f, piercing: %f, bludgeoning: %f)", getSlashingDamage(), getPiercingDamage(), getBludgeoningDamage());
	}
}
