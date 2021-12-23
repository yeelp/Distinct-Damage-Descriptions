package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Sets;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;

/**
 * Mob Resistances. Holds general species specific resistance information, but <em>NOT</em> individual resistance information, use {@link IMobResistances} for that
 * 
 * @author Yeelp
 *
 * @see IMobResistances
 */
public class MobResistanceCategories extends ResistanceCategories {
	private float adaptive;
	private float adaptiveAmount;

	public static final MobResistanceCategories EMPTY = new MobResistanceCategories(new ResistMap(), Sets.newHashSet(), Float.NaN, Float.NaN);
	/**
	 * Build new Mob resistances
	 * 
	 * @param resistances    map of resistances
	 * @param immunities     collection of immunities
	 * @param adaptiveChance chance adaptability is present
	 * @param adaptiveAmount amount resistances change by if adaptive.
	 */
	public MobResistanceCategories(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities, float adaptiveChance, float adaptiveAmount) {
		super(resistances, immunities);
		this.adaptive = adaptiveChance;
		this.adaptiveAmount = adaptiveAmount;
	}

	/**
	 * Get adaptability chance
	 * 
	 * @return adaptability chance
	 */
	public float adaptiveChance() {
		return this.adaptive;
	}

	/**
	 * Get the amount resistances change by on adaptability
	 * 
	 * @return the amount resistances change on adaptability.
	 */
	public float getAdaptiveAmount() {
		return this.adaptiveAmount;
	}
}
