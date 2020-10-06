package yeelp.distinctdamagedescriptions.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private static final List<String> ID_LIST = new ArrayList<String>();
	
	public static final SoundEvent RESIST_DING = createSoundEvent("resist_ding");
	public static final SoundEvent WEAKNESS_HIT = createSoundEvent("weakness_hit");
	public static final SoundEvent IMMUNITY_HIT = createSoundEvent("immunity_hit");
	public static final SoundEvent HIGH_RESIST_HIT = createSoundEvent("high_resist_hit");
	public static final SoundEvent ADAPTABILITY_CHANGE = createSoundEvent("adaptability_change");
	
	public static void init()
	{
		for(Entry<String, SoundEvent> entry : SOUND_MAP.entrySet())
		{
			registerSound(entry.getKey(), entry.getValue());
		}
	}
	
	private static SoundEvent createSoundEvent(String id)
	{
		ResourceLocation loc = new ResourceLocation(ModConsts.MODID, id);
		SoundEvent sound = new SoundEvent(loc);
		SOUND_MAP.put(id, sound);
		ID_LIST.add(loc.toString());
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
			PacketHandler.INSTANCE.sendTo(new SoundMessage(encodeSoundID(name.getRegistryName().toString()), vol, pitch), (EntityPlayerMP) player);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static byte encodeSoundID(String id)
	{
		if(ID_LIST.contains(id))
		{
			return (byte) ID_LIST.indexOf(id);
		}
		else
		{
			throw new RuntimeException(String.format("Can't encode Distinct Damage Description SoundID %s!", id));
		}
	}
	
	public static String decodeSoundID(byte id)
	{
		if(ID_LIST.size() > id) //id is zero indexed, size isn't. Equality edge case not needed.
		{
			return ID_LIST.get(id);
		}
		else
		{
			throw new RuntimeException(String.format("Can't decode Distinct Damage Descriptions SoundID %d, should be no higher than %d", id, ID_LIST.size()-1));
		}
	}
}
