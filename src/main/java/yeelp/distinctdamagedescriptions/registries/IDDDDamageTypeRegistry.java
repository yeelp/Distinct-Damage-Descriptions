package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public interface IDDDDamageTypeRegistry extends IDDDRegistry<DDDDamageType>
{	
	/**
	 * Get the display name for a custom damage type
	 * @param name the internal name, with the "ddd_" prefix.
	 * @return the display name
	 */
	default String getDisplayName(String name)
	{
		return get(name).getDisplayName();
	}
	
	/**
	 * Register information about what in game damage sources count as a particular DDDDamageType
	 * @param type
	 * @param datas
	 */
	void registerDamageTypeData(DDDDamageType type, DamageTypeData... datas);
	
	/**
	 * Get custom damage types
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getCustomDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get extra damage types
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getExtraDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get damage context
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getRegularDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get the full context for a damage source
	 * @param evt
	 * @return the full Set of DDDDamageTypes
	 */
	default Set<DDDDamageType> getFullDamageContext(LivingAttackEvent evt)
	{
		return YMath.setUnion(YMath.setUnion(getCustomDamageContext(evt), getExtraDamageContext(evt)), getRegularDamageContext(evt));
	}
	
	/**
	 * Update the explosion damage from the config
	 */
	void updateExplosionDamage();
}
