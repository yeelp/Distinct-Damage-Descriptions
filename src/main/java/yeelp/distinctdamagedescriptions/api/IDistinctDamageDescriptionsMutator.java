package yeelp.distinctdamagedescriptions.api;

import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.distinctdamagedescriptions.util.DDDDamageType;

public interface IDistinctDamageDescriptionsMutator
{
	/**
	 * Set player resistances
	 * @param player
	 * @param newReists map of new resistances
	 * @param newImmunities set of new immunities, will overwrite old ones.
	 * @param adaptive adaptability status
	 * @param adaptiveAmount amount for adaptability
	 */
	public void setPlayerResistances(EntityPlayer player, Map<DDDDamageType, Float> newReists, Set<DDDDamageType> newImmunities, boolean adaptive, float adaptiveAmount);
}
