package yeelp.distinctdamagedescriptions.api;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.distinctdamagedescriptions.util.ResistMap;

public interface IDistinctDamageDescriptionsMutator {
	/**
	 * Set player resistances
	 * 
	 * @param player
	 * @param newReists      map of new resistances
	 * @param newImmunities  set of new immunities, will overwrite old ones.
	 * @param adaptive       adaptability status
	 * @param adaptiveAmount amount for adaptability
	 */
	public void setPlayerResistances(EntityPlayer player, ResistMap newReists, Set<DDDDamageType> newImmunities, boolean adaptive, float adaptiveAmount);

	/**
	 * Updates an entity's adaptive resistance. Syncs capability if the entity is a
	 * player.
	 * 
	 * @param entity
	 * @param types  types to set adaptive resistance to.
	 * @return true if resistances were updated
	 */
	public boolean updateAdaptiveResistances(EntityLivingBase entity, DDDDamageType... types);
}
