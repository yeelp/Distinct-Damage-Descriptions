package yeelp.distinctdamagedescriptions.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;

public class MobResistancesMessage extends AbstractCapabilityMessage<NBTBase> {
	public MobResistancesMessage(IMobResistances mobResists) {
		super(mobResists);
	}

	public MobResistancesMessage() {
		super();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.deserializeNBT(new PacketBuffer(buf).readCompoundTag());
		}
		catch(IOException e) {
			throw new RuntimeException("Was unable to read NBTTagCompound", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag((NBTTagCompound) this.serializeNBT());
	}

	@Override
	public IMessageHandler<AbstractCapabilityMessage<NBTBase>, IMessage> getMessageHandler() {
		return new CapabilityMessageHandler<NBTBase>((msg, plyer) -> DDDAPI.accessor.getMobResistances(plyer).get().deserializeNBT(msg.serializeNBT()));
	}

}
