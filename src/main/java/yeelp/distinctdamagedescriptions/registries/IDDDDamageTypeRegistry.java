package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	 * @param entityMsg the death message shown when killed by an entity
	 * @param otherMsg the death message shown when killed without an attacker
	 * @param items items that cause this damage type.
	 * @param datas data that determines what causes this damage type.
	 */
	void registerDamageType(String name, String entityMsg, String otherMsg, Set<String> items, DamageTypeData...datas);
	
	/**
	 * Get a damage type
	 * @param originalDamageSource the original damage source
	 * @return a DDDDamageType with additional damage type context, or the originalDamageSource if no additional context could be found.
	 */
	DamageSource getDamageType(DamageSource originalDamageSource);
	
	/**
	 * Get a death message
	 * @param type type of death message
	 * @param defenderName the defender name
	 * @param attackerName the attacker name, may be null
	 * @return the death message
	 */
	String getDeathMessage(@Nonnull String type, @Nonnull String defenderName, @Nullable String attackerName);
}