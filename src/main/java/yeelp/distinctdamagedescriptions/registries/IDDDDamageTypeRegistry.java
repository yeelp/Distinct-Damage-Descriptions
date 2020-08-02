package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;

public interface IDDDDamageTypeRegistry extends IDDDRegistry
{
	/**
	 * register a damage type
	 * @param name name of the damage type
	 * @param items items that cause this damage type.
	 * @param datas data that determines what causes this damage type.
	 */
	void registerDamageType(String name, Set<String> items, DamageTypeData...datas);
	
	/**
	 * Get a damage type
	 * @param originalDamageSource the original damage source
	 * @return a DDDDamageType with additional damage type context, or the originalDamageSource if no additional context could be found.
	 */
	DamageSource getDamageType(DamageSource originalDamageSource);
}
