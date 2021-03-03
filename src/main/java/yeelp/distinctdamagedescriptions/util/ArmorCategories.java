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
	private Map<DDDDamageType, Float> armor, toughness;
	/**
	 * Create a new base armor
	 * @param armorMap the distribution of armor points
	 * @param toughnessMap the distribution of toughness points.
	 */
	public ArmorCategories(Map<DDDDamageType, Float> armorMap, Map<DDDDamageType, Float> toughnessMap)
	{
		this.armor = armorMap;
		this.toughness = toughnessMap;
	}
	
	/**
	 * Get armor for a specific type
	 * @param type
	 * @return armor points
	 */
	public float getArmor(DDDDamageType type)
	{
		return this.armor.get(type);
	}
	
	/**
	 * Get toughness for a specific type
	 * @param type
	 * @return toughness points.
	 */
	public float getToughness(DDDDamageType type)
	{
		return this.toughness.get(type);
	}
	
	public Iterable<Tuple<DDDDamageType, Float>> getNonZeroArmorValues()
	{
		return getNonZeroValues(armor);
	}
	
	public Iterable<Tuple<DDDDamageType, Float>> getNonZeroToughnessValues()
	{
		return getNonZeroValues(toughness);
	}
	
	private Iterable<Tuple<DDDDamageType, Float>> getNonZeroValues(Map<DDDDamageType, Float> map)
	{
		List<Tuple<DDDDamageType, Float>> lst = new LinkedList<Tuple<DDDDamageType, Float>>();
		for(Entry<DDDDamageType, Float> entry : map.entrySet())
		{
			lst.add(new Tuple<DDDDamageType, Float>(entry.getKey(), entry.getValue()));
		}
		return lst;
	}
}
