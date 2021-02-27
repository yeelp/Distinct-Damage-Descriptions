package yeelp.distinctdamagedescriptions.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;

public class MobResistancesMessage extends AbstractCapabilityMessage<NBTTagCompound>
{
	public MobResistancesMessage(IMobResistances mobResists)
	{
		super(mobResists);
	}
	
	public MobResistancesMessage()
	{
		super();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		try
		{
			deserializeNBT(new PacketBuffer(buf).readCompoundTag());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Was unable to read NBTTagCompound", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeCompoundTag(serializeNBT()); 
	}

	@Override
	public IMessageHandler<AbstractCapabilityMessage<NBTTagCompound>, IMessage> getMessageHandler()
	{
		return new CapabilityMessageHandler<NBTTagCompound>((msg, plyer) -> DDDAPI.accessor.getMobResistances((EntityLivingBase) plyer).deserializeNBT(((AbstractCapabilityMessage<NBTTagCompound>)msg).serializeNBT()));
	}

}
