package yeelp.distinctdamagedescriptions.network;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Skeletal framework of an IMessageHandler implementation. Classes that extend
 * this need only provide a method to handle the message
 * 
 * @author Yeelp
 *
 * @param <T> NBT type sent in the REQ of the IMessageHandler
 */
public abstract class AbstractMessageHandler<T extends NBTBase>
		implements IMessageHandler<AbstractCapabilityMessage<T>, IMessage> {
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(AbstractCapabilityMessage<T> msg, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(msg, ctx));
		return null;
	}

	@SideOnly(Side.CLIENT)
	public abstract void handle(AbstractCapabilityMessage<T> msg, MessageContext ctx);
}
