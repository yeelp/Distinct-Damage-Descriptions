package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

/**
 * A wrapped DamageResistance with additional information about mob adaptive
 * immunity.
 * 
 * @author Yeelp
 *
 */
public interface IMobResistances extends IDamageResistances {
	/**
	 * Does this mob have adaptive resistance?
	 * 
	 * @return true if they do, false if they don't.
	 */
	boolean hasAdaptiveResistance();

	/**
	 * Was this mob originally adaptive? Just because a mob was originally adaptive
	 * does not mean that is is now.
	 * 
	 * @return true if this mob was originally adaptive, false if not.
	 */
	boolean isOriginallyAdaptive();

	/**
	 * Get the amount of bonus resistance applied when adaptability triggers.
	 * 
	 * @return the bonus resistance for adaptability.
	 */
	float getAdaptiveAmount();

	/**
	 * Get the amount of bonus resistance applied when adaptability triggers before
	 * any modifiers.
	 * 
	 * @return the base adaptive amount
	 */
	float getBaseAdaptiveAmount();

	/**
	 * Get the adaptive amount when modified by adpative weakness
	 * 
	 * @return the modifed adaptive amount. If adaptive weakness isn't enabled,
	 *         returns the same value as {@link #getAdaptiveAmount()}
	 */
	float getAdaptiveResistanceModified();

	/**
	 * Is this mob adaptive to this type
	 * 
	 * @param type damage type
	 * @return true if the mob's adaptability has triggered for this type.
	 */
	boolean isAdaptiveTo(DDDDamageType type);

	/**
	 * Set the adaptive resistance amount.
	 * 
	 * @param amount
	 */
	void setAdaptiveAmount(float amount);

	/**
	 * Sets the base adaptive resistance amount. The mobs resistances aren't changed
	 * until the next call to {@link #update(EntityLivingBase)}. To update now, also
	 * {@link #setAdaptiveAmount(float)}
	 * 
	 * @param amount
	 */
	void setBaseAdaptiveAmount(float amount);

	/**
	 * Set the adaptive resistance status.
	 * 
	 * @param status    true if this mob should be adaptive resistant, false if not.
	 * @param permanent true if this mob should keep this change across calls to
	 *                  {@link #update(EntityLivingBase)}, false if it should be
	 *                  reset.
	 */
	void setAdaptiveResistance(boolean status, boolean permanent);

	/**
	 * Update adaptive resistance. Doesn't check if this mob is adaptive.
	 * 
	 * @param dmgMap The distribution of damage this mob took
	 * @return true if there was a net change in any resistances
	 */
	boolean updateAdaptiveResistance(DamageMap dmgMap);

	@Override
	IMobResistances copy();

	@Override
	IMobResistances update(EntityLivingBase owner);

	static void register() {
		DDDUpdatableCapabilityBase.register(IMobResistances.class, MobResistances::new);
	}
}
