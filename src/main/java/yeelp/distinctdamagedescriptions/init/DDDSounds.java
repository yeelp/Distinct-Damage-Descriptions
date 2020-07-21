package yeelp.distinctdamagedescriptions.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.network.SoundMessage;

public final class DDDSounds
{
	private static final Map<String, SoundEvent> SOUND_MAP = new HashMap<String, SoundEvent>();
	
	public static final SoundEvent RESIST_DING = createSoundEvent("resist_ding");
	public static final SoundEvent WEAKNESS_HIT = createSoundEvent("weakness_hit");
	public static final SoundEvent IMMUNITY_HIT = createSoundEvent("immunity_hit");
	
	public static void init()
	{
		for(Entry<String, SoundEvent> entry : SOUND_MAP.entrySet())
		{
			registerSound(entry.getKey(), entry.getValue());
		}
	}
	
	private static SoundEvent createSoundEvent(String id)
	{
		SoundEvent sound = new SoundEvent(new ResourceLocation(ModConsts.MODID, id));
		SOUND_MAP.put(id, sound);
		return sound;
	}
	
	private static void registerSound(String name, SoundEvent sound)
	{
		sound.setRegistryName(ModConsts.MODID, name);
		ForgeRegistries.SOUND_EVENTS.register(sound);
	}
	
	public static boolean playSound(EntityPlayer player, SoundEvent name, float vol, float pitch)
	{
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.INSTANCE.sendTo(new SoundMessage(name, vol, pitch), (EntityPlayerMP) player);
			return true;
		}
		else
		{
			return false;
		}
	}
}
