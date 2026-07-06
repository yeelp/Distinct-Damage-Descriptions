package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;

import java.util.Set;

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
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
	 * @param name name of creature type to check.
	 * @return true if this ICreatureType is this type.
	 */
	default boolean isCreatureType(String name) {
		return this.getCreatureTypeNames().contains(name);
	}

	/**
	 * Get how many types this ICreatureType has.
	 * 
	 * @return The count of types in this ICreatureType.
	 */
	default int getTypeCount() {
		return this.getCreatureTypeNames().size();
	}

	/**
	 * Convenience method for checking if an ICreatureType is single typed, may have
	 * applications in certain situations...
	 * <p>
	 * Syntactic sugar for {@code this.getTypeCount() == 1}
	 * 
	 * @return true if this ICreatureType has only ony type.
	 */
	@SuppressWarnings("unused")
	default boolean isSingleTyped() {
		return this.getTypeCount() == 1;
	}

	static void register() {
		DDDCapabilityBase.register(IMobCreatureType.class, NBTTagCompound.class, MobCreatureType::new);
	}
}
