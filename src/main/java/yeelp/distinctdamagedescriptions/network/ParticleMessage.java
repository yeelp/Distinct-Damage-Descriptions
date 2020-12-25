package yeelp.distinctdamagedescriptions.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticle;
import yeelp.distinctdamagedescriptions.client.render.particle.DDDParticleType;

public final class ParticleMessage implements IMessage
{
	private double x, y, z;
	private DDDParticleType type;
	
	public ParticleMessage()
	{
		
	}
	
	public ParticleMessage(DDDParticleType type, double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	public DDDParticleType getType()
	{
		return this.type;
	}
	
	public double[] getCoordinates()
	{
		return new double[] {x,y,z};
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		PacketBuffer pakBuf = new PacketBuffer(buf);
		this.x = pakBuf.readDouble();
		this.y = pakBuf.readDouble();
		this.z = pakBuf.readDouble();
		this.type = DDDParticleType.values()[pakBuf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		PacketBuffer pakBuf = new PacketBuffer(buf);
		pakBuf.writeDouble(this.x);
		pakBuf.writeDouble(this.y);
		pakBuf.writeDouble(this.z);
		pakBuf.writeInt(type.ordinal());
	}
	
	public static final class Handler implements IMessageHandler<ParticleMessage, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(ParticleMessage message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(ParticleMessage msg, MessageContext ctx)
		{
			EntityPlayer receivingPlayer = NetworkHelper.getSidedPlayer(ctx);
			if(receivingPlayer.world.isRemote)
			{
				double[] coords = msg.getCoordinates();
				Minecraft.getMinecraft().effectRenderer.addEffect(new DDDParticle(receivingPlayer.world, coords[0], coords[1], coords[2], 0, 4, 0, msg.getType()));
			}
			else
			{
				DistinctDamageDescriptions.fatal("Particle Message should not be received server side!");
			}
		}
	}

}
