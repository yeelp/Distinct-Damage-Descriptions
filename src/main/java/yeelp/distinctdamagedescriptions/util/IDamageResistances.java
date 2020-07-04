package yeelp.distinctdamagedescriptions.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Damage Resistances capability base.<p>
 * INVARIANT: each resistance <= 1, where 1 is full immunity
 * @author Yeelp
 *
 */
public interface IDamageResistances extends ICapabilitySerializable<NBTTagCompound>
{
	/**
	 * Get Slashing Resistance
	 * @return slashing resistance. Returns 2.0 on full immunity.
	 */
	float getSlashingResistance();
	
	/**
	 * Get Piercing Resistance
	 * @return piercing resistance. Returns 2.0 on full immunity.
	 */
	float getPiercingResistance();
	
	/**
	 * Get Bludgeoning Resistance
	 * @return bludgeoning resistance. Returns 2.0 on full immunity.
	 */
	float getBludgeoningResistance();
	
	/**
	 * Set slashing resistance
	 * @param resist new resistance level
	 * @throws InvariantViolationException if resist > 1
	 */
	void setSlashingResistance(float resist) throws InvariantViolationException;
	
	/**
	 * Set Piercing resistance
	 * @param resist new resistance level
	 * @throws InvariantViolationException if resist > 1
	 */
	void setPiercingResistance(float resist) throws InvariantViolationException;
	
	/**
	 * Set Bludgeoning resistance
	 * @param resist new resistance level
	 * @throws InvariantViolationException if resist > 1
	 */
	void setBludgeoningResistance(float resist) throws InvariantViolationException;
	
	/**
	 * Set Slashing immunity
	 * @param immune status. True if immune, false if susceptible.
	 */
	void setSlashingImmunity(boolean immune);
	
	/**
	 * Set Piercing immunity
	 * @param immune status. True if immune, false if susceptible.
	 */
	void setPiercingImmunity(boolean immune);
	
	/**
	 * Set Bludgeoning immunity
	 * @param immune status. True if immune. false if susceptible.
	 */
	void setBludgeoningImmunity(boolean immune);
	
	/**
	 * Get slashing immunity status
	 * @return true if slashing immune, false otherwise.
	 */
	boolean isSlashingImmune();
	
	/**
	 * Get piercing immunity status
	 * @return true if piercing immune, false otherwise.
	 */
	boolean isPiercingImmune();
	
	/**
	 * Get bludgeoning immunity status
	 * @return true if bludgeoning immune, false otherwise.
	 */
	boolean isBludgeoningImmune();
	
	/**
	 * Convenience method for setting or removing full immunity.
	 * @param immune true if fully immune, false if fully susceptible.
	 */
	default void setFullImmunity(boolean immune)
	{
		setSlashingImmunity(immune);
		setPiercingImmunity(immune);
		setBludgeoningImmunity(immune);
	}
}
