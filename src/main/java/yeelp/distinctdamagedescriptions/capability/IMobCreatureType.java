package yeelp.distinctdamagedescriptions.capability;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;

public interface IMobCreatureType extends DDDCapabilityBase<NBTTagCompound> {
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
	 * Can this creature type be set on fire?
	 * 
	 * @return true if flammable, false if not. A mob that is not flammable does not necessarily make them immune to fire damage. They just won't be set on fire.
	 */
	boolean isFlammable();

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
		DDDCapabilityBase.register(IMobCreatureType.class, NBTTagCompound.class, MobCreatureType::new);
	}
}
