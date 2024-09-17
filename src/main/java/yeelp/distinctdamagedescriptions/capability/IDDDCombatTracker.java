package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.DDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.ArmorClassification;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatContext;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;

/**
 * A combat tracker capability for handling Entity specific damage calculations.
 * 
 * @author Yeelp
 *
 */
public interface IDDDCombatTracker extends DDDCapabilityBase<NBTTagCompound> {

	EntityLivingBase getFighter();

	/**
	 * Get one of the types this entity was last hit by. This type is determined
	 * randomly from the types the entity was hit by and is used for death messages.
	 * 
	 * @return An {@link Optional} wrapping the type the entity was last hit by, or
	 *         an empty Optional if there is no such type.
	 */
	Optional<DDDDamageType> getTypeLastHitBy();

	/**
	 * Get the current {@link ShieldDistribution} the entity is using.
	 * 
	 * @return An {@link Optional} wrapping the used ShieldDistribution or an empty
	 *         Optional if there is no used ShieldDistribution.
	 */
	Optional<ShieldDistribution> getCurrentlyUsedShieldDistribution();

	/**
	 * Get the armor values DDD will use as a modification to the entity's armor
	 * values
	 * 
	 * @return An {@link Optional} wrapping the map of equipment slot to armor
	 *         values or an empty Optional if there are no such values.
	 */
	Optional<Map<EntityEquipmentSlot, ArmorValues>> getNewDeltaArmorValues();

	/**
	 * Get a map of the incoming damage.
	 * 
	 * @return An {@link Optional} wrapping the incoming damage map or an empty
	 *         Optional if there is no such map.
	 */
	Optional<DamageMap> getIncomingDamage();

	/**
	 * Get the armor classification used in armor calculations.
	 * 
	 * @return An {@link Optional} wrapping the {@link ArmorClassification} or an
	 *         empty Optional if there was no armor classified or armor
	 *         classification has no occured yet.
	 */
	Optional<ArmorClassification> getArmorClassification();

	/**
	 * Get the damage reference value DDD uses for damage calculations. This value
	 * is used to determine if the damage context has drastically changed since DDD
	 * last updated the context.
	 * 
	 * @return The damage reference value.
	 */
	float getDamageReference();

	/**
	 * Get the last {@link CombatResults} that DDD produced from the last damage
	 * calculation.
	 * 
	 * @return The most recent combat results.
	 */
	CombatResults getRecentResults();

	/**
	 * Get the {@link CombatContext}; the context where the damage calculation is
	 * taking place in.
	 * 
	 * @return The combat context.
	 */
	CombatContext getCombatContext();

	/**
	 * Return if the damage was classified or not. Classified in this context
	 * doesn't mean DDD found a valid damage distribution, it just means that it
	 * tried already.
	 * 
	 * @return If damage classification occurred.
	 */
	boolean wasDamageClassified();

	/**
	 * Set the type that DDD will use in death messages.
	 * 
	 * @param type The type DDD will get the death message from.
	 */
	void setTypeLastHitBy(DDDDamageType type);

	/**
	 * Set the {@link ShieldDistribution} used in this shield calculation.
	 * 
	 * @param dist The ShieldDistribution.
	 */
	void setUsedShieldDistribution(ShieldDistribution dist);

	/**
	 * Set the {@link CombatContext} used in this damage calculation.
	 * 
	 * @param ctx The combat context
	 */
	void setCombatContext(CombatContext ctx);

	/**
	 * Set the new change in armor values per equipment slot
	 * 
	 * @param vals A map that maps equipment slot to the change in armor values.
	 */
	void setNewDeltaArmorValues(Map<EntityEquipmentSlot, ArmorValues> vals);

	/**
	 * Set the incoming damage map
	 * 
	 * @param map the damage map.
	 */
	void setIncomingDamage(DamageMap map);

	/**
	 * Set the damage reference that DDD uses to determine if the damage context and
	 * damage map should be updated.
	 * 
	 * @param amount the damage reference.
	 */
	void setDamageReference(float amount);

	/**
	 * Wipe the most recent results.
	 */
	void wipeResults();

	/**
	 * Set if the damage was classified or not
	 * 
	 * @param state the state of classification.
	 */
	void setDamageClassified(boolean state);

	/**
	 * Handle the Attack stage of damage.
	 * 
	 * @param evt The LivingAttackEvent.
	 */
	void handleAttackStage(LivingAttackEvent evt);

	/**
	 * Handle the Hurt stage of damage.
	 * 
	 * @param evt The LivingHurtEvent.
	 */
	void handleHurtStage(LivingHurtEvent evt);

	/**
	 * Handle the Damage stage of damage.
	 * 
	 * @param evt The LivingDamageEvent.
	 */
	void handleDamageStage(LivingDamageEvent evt);

	/**
	 * Clear the tracker and reset all its values.
	 */
	default void clear() {
		if(this.getFighter().isEntityAlive()) {
			this.setTypeLastHitBy(null);
			this.setCombatContext(null);
		}
		this.setIncomingDamage(null);
		this.setNewDeltaArmorValues(null);
		this.setUsedShieldDistribution(null);
		this.wipeResults();
		this.setDamageClassified(false);
	}

	static void register() {
		DDDCapabilityBase.register(IDDDCombatTracker.class, NBTTagCompound.class, () -> new DDDCombatTracker(null));
	}
}
