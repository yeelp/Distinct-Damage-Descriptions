package yeelp.distinctdamagedescriptions.api;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;

public interface IDistinctDamageDescriptionsAccessor
{
	/**
	 * Get the slashing damage this entity does
	 * @param entity
	 * @return that entity's slashing damage
	 */
	double getSlashingDamage(EntityLivingBase entity);
	
	/**
	 * Get the piercing damage this entity does
	 * @param entity
	 * @return that entity's piercing damage
	 */
	double getPiercingDamage(EntityLivingBase entity);
	
	/**
	 * Get the bludgeoning damage this entity does
	 * @param entity
	 * @return that entity's bludgeoning damage
	 */
	double getBludgeoningDamage(EntityLivingBase entity);
	
	/**
	 * Get an entity's slashing resistance
	 * @param entity
	 * @return that entity's slashing resistance.
	 */
	double getSlashingResistance(EntityLivingBase entity);
	
	/**
	 * Get an entity's piercing resistance
	 * @param entity
	 * @return that entity's piercing resistance
	 */
	double getPiercingResistance(EntityLivingBase entity);
	/**
	 * Get an entity's bludgeoning resistance
	 * @param entity
	 * @return that entity's bludgeoning resistance.
	 */
	double getBludgeoningResistance(EntityLivingBase entity);
}
