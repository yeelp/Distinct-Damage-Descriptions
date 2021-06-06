package yeelp.distinctdamagedescriptions.network;

import java.util.function.BiConsumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A simple message handler for capabilities. Need only provide a BiConsumer for
 * handling messages.
 * 
 * @author Yeelp
 *
 * @param <T> NBT sent via the IMessageHandler
 */
public final class CapabilityMessageHandler<T extends NBTBase> extends AbstractMessageHandler<T> {
	private BiConsumer<AbstractCapabilityMessage<T>, EntityPlayer> handler;

	public CapabilityMessageHandler(BiConsumer<AbstractCapabilityMessage<T>, EntityPlayer> handler) {
		this.handler = handler;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handle(AbstractCapabilityMessage<T> msg, MessageContext ctx) {
		EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
		handler.accept(msg, player);
	}
}
