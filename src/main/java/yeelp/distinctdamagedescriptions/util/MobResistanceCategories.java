package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * Mob Resistances. Used on startup.
 * @author Yeelp
 *
 */
public class MobResistanceCategories extends ResistanceCategories
{
	private float adaptive;
	private float adaptiveAmount;
	
	/**
	 * Build new Mob resistances
	 * @param resistances map of resistances
	 * @param immunities collection of immunities
	 * @param adaptiveChance chance adaptability is present
	 * @param adaptiveAmount amount resistances change by if adaptive.
	 */
	public MobResistanceCategories(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities, float adaptiveChance, float adaptiveAmount)
	{
		super(resistances, immunities);
		this.adaptive = adaptiveChance;
		this.adaptiveAmount = adaptiveAmount;
	}

	/**
	 * Get adaptability chance
	 * @return adaptability chance
	 */
	public float adaptiveChance()
	{
		return adaptive;
	}
	
	/**
	 * Get the amount resistances change by on adaptability
	 * @return the amount resistances change on adaptability.
	 */
	public float getAdaptiveAmount()
	{
		return adaptiveAmount;
	}
}
