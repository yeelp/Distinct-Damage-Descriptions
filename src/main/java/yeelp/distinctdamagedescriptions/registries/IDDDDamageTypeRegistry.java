package yeelp.distinctdamagedescriptions.registries;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;

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
	 * Get damage types for a damage source
	 * @param src
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getCustomDamageTypes(DamageSource src);
}
