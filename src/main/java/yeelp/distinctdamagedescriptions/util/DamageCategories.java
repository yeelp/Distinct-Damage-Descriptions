package yeelp.distinctdamagedescriptions.util;

import java.util.Map;

import net.minecraft.util.Tuple;

/**
 * A wrapped Map for storing damage values split across categories.
 * @author Yeelp
 *
 */
public final class DamageCategories
{
	private Map<String, Float> dmg;
	/**
	 * Create a new DamageCategories
	 * @param dmges an array of tuples of the form {@code(type, dmg)} where:
	 *        {@code type} is the type of damage
	 *        {@code dmg} is the amount of damage that type inflicted.
	 */
	@SafeVarargs
	public DamageCategories(Tuple<String, Float>... dmges)
	{
		this.dmg = new NonNullMap<String, Float>(0.0f);
		for(Tuple<String, Float> t : dmges)
		{
			this.dmg.put(t.getFirst(), t.getSecond());
		}
	}
	
	public DamageCategories(Map<String, Float> dmges)
	{
		this.dmg = dmges;
	}
	
	
	public float getDamage(String type)
	{
		return this.dmg.get(type);
	}
	
	@Override
	public String toString()
	{
		return String.format("(slashing: %f, piercing: %f, bludgeoning: %f)", getDamage("slashing"), getDamage("piercing"), getDamage("bludgeoning"));
	}
	
	protected Map<String, Float> getDistribution()
	{
		return this.dmg;
	}
}
