package yeelp.distinctdamagedescriptions.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IDistinctDamageDescriptionsMutator
{
	/**
	 * Set player resistances
	 * @param player 
	 * @param slash slashing resistance
	 * @param pierce piercing resistance
	 * @param bludge bludgeoning resistance
	 * @param slashImmune slashing immunity
	 * @param pierceImmune piercing immunity
	 * @param bludgeImmune bludgeoning immunity
	 * @param adaptive adaptability status
	 */
	public void setPlayerResistances(EntityPlayer player, float slash, float pierce, float bludge, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, boolean adaptive);
}
