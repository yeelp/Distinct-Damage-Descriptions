package yeelp.distinctdamagedescriptions.api.impl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.potion.Potion;
import yeelp.distinctdamagedescriptions.api.IHasCreationSource;

import java.util.Objects;
import java.util.Set;

/**
 * Data storage for CreatureTypes
 * 
 * @author Yeelp
 *
 */
public final class CreatureType implements IHasCreationSource {
	/**
	 * Default CreatureType. Falls back to this if no CreatureTypeData applies.
	 * Has no special properties, and can not be modified.
	 */
	public static final CreatureType UNKNOWN = new CreatureType();

	private final Source src;
	private final String type;
	private final Set<String> potionImmunities;
	public boolean criticalImmunity;
	public boolean flammable;

	/**
	 * Build a new CreatureType with a creation Source of {@link Source#JSON}.
	 * 
	 * @param name name of the creature type used internally.
	 * @param potionImmunities set of potion effect strings this creature type is
	 *                         immune to.
	 * @param criticalImmunity if this creature type is immune to criticals.
	 * @param flammable if this creature type is flammable or not.
	 */
	public CreatureType(String name, Set<String> potionImmunities, boolean criticalImmunity, boolean flammable) {
		this(name, potionImmunities, criticalImmunity, flammable, Source.JSON);
	}
	
	/**
	 * Build a new CreatureType
	 * @param name name of the creature type used internally.
	 * @param potionImmunities set of potion effect strings this creature type is immune to.
	 * @param criticalImmunity if this creature type is immune to criticals.
	 * @param flammable if this creature type is flammable or not.
	 * @param src the {@link Source} that created this CreatureTypeData
	 */
	public CreatureType(String name, Set<String> potionImmunities, boolean criticalImmunity, boolean flammable, Source src) {
		this.type = name;
		this.potionImmunities = potionImmunities;
		this.criticalImmunity = criticalImmunity;
		this.flammable = flammable;
		this.src = src;
	}

	private CreatureType() {
		this("unknown", ImmutableSet.of(), false, true, Source.BUILTIN);
	}

	/**
	 * Get the type name
	 * 
	 * @return type name
	 */
	public String getTypeName() {
		return this.type;
	}

	/**
	 * Get a set of potion immunities
	 * 
	 * @return the set of potions this creature type is immune to.
	 */
	public Set<String> getPotionImmunities() {
		return this.potionImmunities;
	}
	
	/**
	 * Add a potion immunity to this creature type
	 * @param potion Potion immunity to add.
	 */
	public void addPotionImmunity(Potion potion) {
		this.potionImmunities.add(Objects.requireNonNull(potion.getRegistryName()).toString());
	}
	
	public void removePotionImmunity(Potion potion) {
		this.potionImmunities.remove(Objects.requireNonNull(potion.getRegistryName()).toString());
	}

	/**
	 * Is this CreatureType providing crit immunity?
	 * 
	 * @return true if immune, false if not
	 */
	public boolean isImmuneToCriticals() {
		return this.criticalImmunity;
	}
	
	/**
	 * Is this CreatureType flammable?
	 * 
	 * @return true if flammable, false if not
	 */
	public boolean isFlammable() {
		return this.flammable;
	}

	@Override
	public Source getCreationSource() {
		return this.src;
	}

	@Override
	public String toString() {
		return String.format("%s (%s) (Critical Hit Immunity: %s, Flammable: %s, Potion Immunities: %s)", this.type, this.src.toString(), this.criticalImmunity, this.flammable, this.potionImmunities);
	}
}
