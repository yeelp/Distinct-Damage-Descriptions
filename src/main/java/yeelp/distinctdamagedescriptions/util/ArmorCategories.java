package yeelp.distinctdamagedescriptions.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.Tuple;

/**
 * A container to store armor and toughness values once distributed.
 * @author Yeelp
 *
 */
public final class ArmorCategories
{
	private Map<String, Float> armor, toughness;
	/**
	 * Create a new base armor
	 * @param armorMap the distribution of armor points
	 * @param toughnessMap the distribution of toughness points.
	 */
	public ArmorCategories(Map<String, Float> armorMap, Map<String, Float> toughnessMap)
	{
		this.armor = armorMap;
		this.toughness = toughnessMap;
	}
	
	/**
	 * Get armor for a specific type
	 * @param type
	 * @return armor points
	 */
	public float getArmor(String type)
	{
		return this.armor.get(type);
	}
	
	/**
	 * Get toughness for a specific type
	 * @param type
	 * @return toughness points.
	 */
	public float getToughness(String type)
	{
		return this.toughness.get(type);
	}
	
	public Iterable<Tuple<String, Float>> getNonZeroArmorValues()
	{
		return getNonZeroValues(armor);
	}
	
	public Iterable<Tuple<String, Float>> getNonZeroToughnessValues()
	{
		return getNonZeroValues(toughness);
	}
	
	private Iterable<Tuple<String, Float>> getNonZeroValues(Map<String, Float> map)
	{
		List<Tuple<String, Float>> lst = new LinkedList<Tuple<String, Float>>();
		for(Entry<String, Float> entry : map.entrySet())
		{
			lst.add(new Tuple<String, Float>(entry.getKey(), entry.getValue()));
		}
		return lst;
	}
}
