package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.ArmorClassification;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults.ResultsBuilder;

/**
 * A damage calculation. Stores all the values relevent to this calculation.
 * 
 * @author Yeelp
 *
 */
public final class DamageCalculation {
	private DDDDamageType type = null;
	private final CombatContext ctx;
	private float damage;
	private ResultsBuilder results = new ResultsBuilder();
	private ArmorClassification armorClassification;
	private ShieldDistribution shieldDist = null;
	private Map<EntityEquipmentSlot, ArmorValues> armor = null;
	private DamageMap incomingDamage = null;
	private boolean wasDamageClassified = false;
	private boolean wasArmorModified = false;
	private boolean completed = false;

	/**
	 * Create a new DamageCalculation based off the supplied {@link CombatContext}.
	 * 
	 * @param ctx The context for the calculation.
	 */
	public DamageCalculation(CombatContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Get one of the types this entity was last hit by. This type is determined
	 * randomly from the types the entity was hit by and is used for death messages.
	 * 
	 * @return An {@link Optional} wrapping the type the entity was last hit by, or
	 *         an empty Optional if there is no such type.
	 */
	public Optional<DDDDamageType> getType() {
		return Optional.ofNullable(this.type);
	}

	/**
	 * Set the type that DDD will use in death messages.
	 * 
	 * @param type The type DDD will get the death message from.
	 */
	public void setType(DDDDamageType type) {
		this.type = type;
	}

	/**
	 * Get the damage reference value DDD uses for damage calculations. This value
	 * is used to determine if the damage context has drastically changed since DDD
	 * last updated the context.
	 * 
	 * @return The damage reference value.
	 */
	public float getDamage() {
		return this.damage;
	}

	/**
	 * Set the damage reference that DDD uses to determine if the damage context and
	 * damage map should be updated.
	 * 
	 * @param amount the damage reference.
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	public ResultsBuilder getResultsBuilder() {
		return this.results;
	}

	/**
	 * Get the armor classification used in armor calculations.
	 * 
	 * @return An {@link Optional} wrapping the {@link ArmorClassification} or an
	 *         empty Optional if there was no armor classified or armor
	 *         classification has no occured yet.
	 */
	public ArmorClassification getArmorClassification() {
		return this.armorClassification;
	}

	public void setArmorClassification(ArmorClassification armorClassification) {
		this.armorClassification = armorClassification;
	}

	public CombatContext getContext() {
		return this.ctx;
	}

	/**
	 * Return if the damage was classified or not. Classified in this context
	 * doesn't mean DDD found a valid damage distribution, it just means that it
	 * tried already.
	 * 
	 * @return If damage classification occurred.
	 */
	public boolean wasDamageClassified() {
		return this.wasDamageClassified;
	}

	/**
	 * Get the current {@link ShieldDistribution} the entity is using.
	 * 
	 * @return An {@link Optional} wrapping the used ShieldDistribution or an empty
	 *         Optional if there is no used ShieldDistribution.
	 */
	public Optional<ShieldDistribution> getShieldDist() {
		return Optional.ofNullable(this.shieldDist);
	}

	/**
	 * Set the {@link ShieldDistribution} used in this shield calculation.
	 * 
	 * @param shieldDist The ShieldDistribution.
	 */
	public void setShieldDist(ShieldDistribution shieldDist) {
		this.shieldDist = shieldDist;
	}

	/**
	 * Get the armor values DDD will use as a modification to the entity's armor
	 * values
	 * 
	 * @return An {@link Optional} wrapping the map of equipment slot to armor
	 *         values or an empty Optional if there are no such values.
	 */
	public Optional<Map<EntityEquipmentSlot, ArmorValues>> getDeltaArmor() {
		return Optional.ofNullable(this.armor);
	}

	/**
	 * Create a new map to track the change in armor values per equipment slot.
	 */
	public void setNewArmorValuesMap() {
		this.armor = Maps.newHashMap();
	}
	
	public void markCompleted() {
		this.completed = true;
	}
	
	public boolean wasCompleted() {
		return this.completed;
	}
	
	public void markArmorModified() {
		this.wasArmorModified = true;
	}
	
	public boolean wasArmorModified() {
		return this.wasArmorModified;
	}

	public DamageMap getClassifiedDamage() {
		return this.incomingDamage;
	}

	public void classifyDamage() {
		this.incomingDamage = DDDCombatCalculations.classifyDamage(this.getContext()).orElse(null);
		this.wasDamageClassified = true;
	}
}
