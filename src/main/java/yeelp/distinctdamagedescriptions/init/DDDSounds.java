package yeelp.distinctdamagedescriptions.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.network.SoundMessage;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.CombatResults;

public final class DDDSounds {
	private static final Map<String, SoundEvent> SOUND_MAP = new HashMap<String, SoundEvent>();
	private static final List<String> ID_LIST = new ArrayList<String>();

	public enum DDDCombatSounds {
		RESIST(RESIST_DING) {
			@Override
			protected boolean isApplicable(CombatResults results, boolean forAttacker) {
				return forAttacker && results.wasResistanceHit() && isRatioWithinBounds(0, 1, results.getRatio());
			}

			@Override
			public float getRecommendedVolume() {
				return 1.7f;
			}
		},
		WEAKNESS(WEAKNESS_HIT) {
			@Override
			protected boolean isApplicable(CombatResults results, boolean forAttacker) {
				return forAttacker && results.wasWeaknessHit() && isRatioWithinBounds(1, Double.MAX_VALUE, results.getRatio());
			}

			@Override
			public float getRecommendedVolume() {
				return 0.6f;
			}
		},
		IMMUNITY(IMMUNITY_HIT) {
			@Override
			protected boolean isApplicable(CombatResults results, boolean forAttacker) {
				return (forAttacker && results.wasImmunityTriggered() && results.getAmount().orElse(Double.NaN) == 0) || (!forAttacker && results.wasShieldEffective() && results.getShieldRatio().orElse(1) == 0);
			}

			@Override
			public float getRecommendedVolume() {
				return 1.5f;
			}
		},
		RESIST_NULLIFY(HIGH_RESIST_HIT) {
			@Override
			protected boolean isApplicable(CombatResults results, boolean forAttacker) {
				return (forAttacker && !results.wasImmunityTriggered() && results.wasResistanceHit() && results.getAmount().orElse(Double.NaN) == 0) || (!forAttacker && results.wasShieldEffective() && isRatioWithinBounds(0, 1, results.getShieldRatio()));
			}

			@Override
			public float getRecommendedVolume() {
				return 1.7f;
			}
		},
		ADAPTABILITY(ADAPTABILITY_CHANGE) {
			@Override
			protected boolean isApplicable(CombatResults results, boolean forAttacker) {
				return forAttacker && results.wasAdaptabilityTriggered();
			}

			@Override
			public float getRecommendedVolume() {
				return 2.0f;
			}
		};

		private SoundEvent evt;

		private DDDCombatSounds(SoundEvent evt) {
			this.evt = evt;
		}

		public Optional<SoundEvent> getSoundIfApplicable(CombatResults results, boolean attacker) {
			return Optional.ofNullable(this.isApplicable(results, attacker) ? this.evt : null);
		}

		protected abstract boolean isApplicable(CombatResults results, boolean forAttacker);

		public abstract float getRecommendedVolume();

		protected static boolean isRatioWithinBounds(double lo, double hi, OptionalDouble opt) {
			if(opt.isPresent()) {
				return lo < opt.getAsDouble() && opt.getAsDouble() < hi;
			}
			return false;
		}
	}

	public static final SoundEvent RESIST_DING = createSoundEvent("resist_ding");
	public static final SoundEvent WEAKNESS_HIT = createSoundEvent("weakness_hit");
	public static final SoundEvent IMMUNITY_HIT = createSoundEvent("immunity_hit");
	public static final SoundEvent HIGH_RESIST_HIT = createSoundEvent("high_resist_hit");
	public static final SoundEvent ADAPTABILITY_CHANGE = createSoundEvent("adaptability_change");
	public static final SoundEvent DISTINCTION = createSoundEvent("distinction");

	public static void init() {
		SOUND_MAP.forEach(DDDSounds::registerSound);
	}

	private static SoundEvent createSoundEvent(String id) {
		ResourceLocation loc = new ResourceLocation(ModConsts.MODID, id);
		SoundEvent sound = new SoundEvent(loc);
		SOUND_MAP.put(id, sound);
		ID_LIST.add(loc.toString());
		return sound;
	}

	private static void registerSound(String name, SoundEvent sound) {
		sound.setRegistryName(ModConsts.MODID, name);
		ForgeRegistries.SOUND_EVENTS.register(sound);
	}

	public static boolean playSound(EntityPlayer player, SoundEvent name, float vol, float pitch) {
		if(player instanceof EntityPlayerMP) {
			PacketHandler.INSTANCE.sendTo(new SoundMessage(encodeSoundID(name.getRegistryName().toString()), vol, pitch), (EntityPlayerMP) player);
			return true;
		}
		return false;
	}

	public static byte encodeSoundID(String id) {
		if(ID_LIST.contains(id)) {
			return (byte) ID_LIST.indexOf(id);
		}
		throw new RuntimeException(String.format("Can't encode Distinct Damage Description SoundID %s!", id));
	}

	public static String decodeSoundID(byte id) {
		if(ID_LIST.size() > id) // id is zero indexed, size isn't. Equality edge case not needed.
		{
			return ID_LIST.get(id);
		}
		throw new RuntimeException(String.format("Can't decode Distinct Damage Descriptions SoundID %d, should be no higher than %d", id, ID_LIST.size() - 1));
	}
}
