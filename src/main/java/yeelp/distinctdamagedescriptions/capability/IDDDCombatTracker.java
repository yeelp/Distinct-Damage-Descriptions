package yeelp.distinctdamagedescriptions.capability;

import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.DDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatContext;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;

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
	 * @return An {@link Optional} wrapping the armor values or an empty Optional if
	 *         there are no such values.
	 */
	Optional<ArmorValues> getNewArmorValues();

	/**
	 * Get a map of the incoming damage.
	 * 
	 * @return An {@link Optional} wrapping the incoming damage map or an empty
	 *         Optional if there is no such map.
	 */
	Optional<DamageMap> getIncomingDamage();

	float getDamageReference();

	CombatResults getRecentResults();

	CombatContext getCombatContext();
	
	/**
	 * Return if the damage was classified or not. Classified in this context doesn't mean DDD found a valid damage distribution, it just means that it tried already. 
	 * @return If damage classification occurred.
	 */
	boolean wasDamageClassified();
	
	void setTypeLastHitBy(DDDDamageType type);
	
	void setUsedShieldDistribution(ShieldDistribution dist);
	
	void setCombatContext(CombatContext ctx);
	
	void setNewArmorValues(ArmorValues vals);
	
	void setIncomingDamage(DamageMap map);
	
	void setDamageReference(float amount);
	
	void wipeResults();
	
	void setDamageClassified(boolean state);
	
	void handleAttackStage(LivingAttackEvent evt);
	
	void handleHurtStage(LivingHurtEvent evt);
	
	void handleDamageStage(LivingDamageEvent evt);
	
	default void clear() {
		if(this.getFighter().isEntityAlive()) {
			this.setTypeLastHitBy(null);
			this.setCombatContext(null);
		}
		this.setIncomingDamage(null);
		this.setNewArmorValues(null);
		this.setUsedShieldDistribution(null);
		this.wipeResults();
		this.setDamageClassified(false);
	}

	static void register() {
		DDDCapabilityBase.register(IDDDCombatTracker.class, NBTTagCompound.class, () -> new DDDCombatTracker(null));
	}
}
