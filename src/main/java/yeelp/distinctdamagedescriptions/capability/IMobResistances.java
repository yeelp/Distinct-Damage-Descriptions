package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.util.DamageMap;

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
	 * Get the amount of bonus resistance applied when adaptability triggers.
	 * 
	 * @return the bonus resistance for adaptability.
	 */
	float getAdaptiveAmount();
	
	/**
	 * Get the adaptive amount when modified by adpative weakness
	 * @return the modifed adaptive amount. If adaptive weakness isn't enabled, returns the same value as {@link #getAdaptiveAmount()}
	 */
	float getAdaptiveResistanceModified();
	
	/**
	 * Is this mob adaptive to this type
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
	 * Set the adaptive resistance status.
	 * 
	 * @param status true if this mob should be adaptive resistant, false if not.
	 */
	void setAdaptiveResistance(boolean status);

	/**
	 * Update adaptive resistance. Doesn't check if this mob is adaptive.
	 * 
	 * @param dmgMap The distribution of damage this mob took
	 * @return true if there was a net change in any resistances
	 */
	boolean updateAdaptiveResistance(DamageMap dmgMap);
	
	static void register() {
		DDDCapabilityBase.register(IMobResistances.class, NBTTagCompound.class, MobResistances::new);
	}
}
