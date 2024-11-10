package yeelp.distinctdamagedescriptions.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.init.DDDSounds;

public final class SoundMessage implements IMessage {
	private byte id;
	private float volume;
	private float pitch;

	public SoundMessage() {

	}

	public SoundMessage(byte id, float volume, float pitch) {
		this.id = id;
		this.volume = volume;
		this.pitch = pitch;
	}

	public byte getSoundID() {
		return this.id;
	}

	public float getVolume() {
		return this.volume;
	}

	public float getPitch() {
		return this.pitch;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pakBuf = new PacketBuffer(buf);
		this.id = pakBuf.readByte();
		this.volume = pakBuf.readFloat();
		this.pitch = pakBuf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pakBuf = new PacketBuffer(buf);
		pakBuf.writeByte(this.id);
		pakBuf.writeFloat(this.volume);
		pakBuf.writeFloat(this.pitch);
	}

	public static final class Handler implements IMessageHandler<SoundMessage, IMessage> {
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(SoundMessage message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		@SideOnly(Side.CLIENT)
		public static void handle(SoundMessage msg, MessageContext ctx) {
			if(!ModConfig.client.enableSfx) {
				return;
			}
			EntityPlayer player = NetworkHelper.getSidedPlayer(ctx);
			if(player != null) {
				SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(DDDSounds.decodeSoundID(msg.getSoundID())));
				if(sound != null) {
					player.playSound(sound, msg.getVolume(), msg.getPitch());
				}
			}
		}
	}
}
