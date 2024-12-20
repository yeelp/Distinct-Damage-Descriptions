package yeelp.distinctdamagedescriptions.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;

/**
 * A capability that can be synced to players.
 * 
 * @author Yeelp
 *
 */
public interface ISyncableCapability {

	/**
	 * Get the message to sync with
	 * 
	 * @return the IMessage
	 */
	IMessage getIMessage();

	/**
	 * Sync capability to this player.
	 * 
	 * @param player
	 */
	default void sync(EntityPlayer player) {
		if(!player.world.isRemote) {
			PacketHandler.INSTANCE.sendTo(this.getIMessage(), (EntityPlayerMP) player);
		}
	}
}
