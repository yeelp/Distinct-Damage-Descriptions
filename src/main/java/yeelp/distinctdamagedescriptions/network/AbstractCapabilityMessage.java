package yeelp.distinctdamagedescriptions.network;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

/**
 * Skeletal framework of capability network message. Message classes need only
 * to extend this class and provide IMessage implementations.
 * 
 * @author Yeelp
 *
 * @param <T> The NBT type sent via this message.
 */
public abstract class AbstractCapabilityMessage<T extends NBTBase> implements IMessage {
	private T nbt;

	public AbstractCapabilityMessage() {

	}

	public AbstractCapabilityMessage(ICapabilitySerializable<T> capability) {
		nbt = capability.serializeNBT();
	}

	public T serializeNBT() {
		return this.nbt;
	}

	@SuppressWarnings("unchecked")
	public void deserializeNBT(T nbt) {
		this.nbt = (T) nbt.copy();
	}

	public abstract IMessageHandler<AbstractCapabilityMessage<T>, IMessage> getMessageHandler();
}
