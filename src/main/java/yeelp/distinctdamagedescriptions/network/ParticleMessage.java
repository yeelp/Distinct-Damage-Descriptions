package yeelp.distinctdamagedescriptions.network;

import java.util.List;

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
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.DDDEffects.ParticleInfo;

public final class ParticleMessage implements IMessage {
	private ParticleInfo[] infos = new ParticleInfo[0];

	public ParticleMessage() {

	}

	public ParticleMessage(List<ParticleInfo> infos) {
		this.infos = infos.toArray(this.infos);
	}

	public ParticleInfo[] getInfos() {
		return this.infos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pakBuf = new PacketBuffer(buf);
		this.infos = new ParticleInfo[pakBuf.readInt()];
		for(int i = 0; i < this.infos.length; i++) {
			this.infos[i] = new ParticleInfo(pakBuf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pakBuf = new PacketBuffer(buf);
		pakBuf.writeInt(this.infos.length);
		for(ParticleInfo info : this.infos) {
			info.toBytes(pakBuf);
		}
	}

	public static final class Handler implements IMessageHandler<ParticleMessage, IMessage> {
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(ParticleMessage message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		@SideOnly(Side.CLIENT)
		public static void handle(ParticleMessage msg, MessageContext ctx) {
			if(!ModConfig.client.enableParticles) {
				return;
			}
			EntityPlayer receivingPlayer = NetworkHelper.getSidedPlayer(ctx);
			if(receivingPlayer.world.isRemote) {
				for(ParticleInfo info : msg.getInfos()) {
					double[] coords = info.getCoordinates();
					Minecraft.getMinecraft().effectRenderer.addEffect(new DDDParticle(receivingPlayer.world, coords[0], coords[1], coords[2], 0, 4, 0, info.getType()));
				}
			}
			else {
				DistinctDamageDescriptions.fatal("Particle Message should not be received server side!");
			}
		}
	}

}
