package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;

public interface IDDDDamageTypeRegistry extends IDDDRegistry
{
	/**
	 * register a damage type
	 * @param name name of the damage type
	 * @param displayName the name used in tooltips
	 * @param colour the colour used in tooltips.
	 * @param entityMsg the death message shown when killed by an entity
	 * @param otherMsg the death message shown when killed without an attacker
	 * @param datas data that determines what causes this damage type.
	 */
	void registerDamageType(String name, String displayName, TextFormatting colour, String entityMsg, String otherMsg, DamageTypeData...datas);
	
	/**
	 * Get the display name for a custom damage type
	 * @param name the internal name, with the "ddd_" prefix.
	 * @return the display name
	 */
	String getDisplayName(String name);
	
	/**
	 * Get a damage type
	 * @param originalDamageSource the original damage source
	 * @return a set of Strings with additional damage type context, or the empty set if no additional context was found.
	 */
	Set<String> getCustomDamageContext(DamageSource originalDamageSource);
	
	/**
	 * Get a death message
	 * @param type type of death message
	 * @param defenderName the defender name
	 * @param attackerName the attacker name, may be null
	 * @return the death message
	 */
	String getDeathMessage(@Nonnull String type, @Nonnull String defenderName, @Nullable String attackerName);
}
