package yeelp.distinctdamagedescriptions.capability;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;

public interface ICreatureType extends DDDCapabilityBase<NBTTagCompound> {
	/**
	 * Return a set of all the creature type names.
	 * 
	 * @return a Set of all the creature type names
	 */
	Set<String> getCreatureTypeNames();

	/**
	 * Is this creature type immune to a PotionEffect?
	 * 
	 * @param effect the Potion effect
	 * @return true if immune, false if not.
	 */
	boolean isImmuneToPotionEffect(PotionEffect effect);

	/**
	 * Is this creature type immune to critical hits?
	 * 
	 * @return true if immune, false if not.
	 */
	boolean isImmuneToCriticalHits();

	/**
	 * Check if this ICreatureType is a certain type.
	 * 
	 * @param name
	 * @return
	 */
	default boolean isCreatureType(String name) {
		return getCreatureTypeNames().contains(name);
	}

	/**
	 * Get how many types this ICreatureType has.
	 * 
	 * @return
	 */
	default int getTypeCount() {
		return getCreatureTypeNames().size();
	}

	/**
	 * Convenience method for checking if an ICreatureType is single typed, may have
	 * applications in certain situations...
	 * 
	 * @return
	 */
	default boolean isSingleTyped() {
		return getTypeCount() == 1;
	}
	
	static void register() {
		DDDCapabilityBase.register(ICreatureType.class, NBTTagCompound.class, CreatureType::new);
	}
}
