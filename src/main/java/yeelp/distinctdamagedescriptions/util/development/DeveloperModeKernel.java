package yeelp.distinctdamagedescriptions.util.development;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;

/**
 * A kernel for collecting all the info callbacks in DDD's Developer Mode. This
 * has been refactored from a hard-coded set of callbacks to a dynamic set,
 * allowing mod integration to register callbacks using DDD's system.
 * <p>
 * Although mainly for internal use, it is available to other sources. You just
 * need a config option that uses DDD's {@link DeveloperStatus} enum as its
 * config option and then provide an implementation of
 * {@link IDeveloperModeInfoCallback}. Register the callback with
 * {@link #registerCallback(Class, IDeveloperModeInfoCallback)}, specifying the
 * event class you want the callback fired on. If DDD doesn't normally fire
 * callbacks on that event, you can create a handler to call
 * {@link DeveloperModeKernel#fireCallbacks(Event)}.
 * <p>
 * You can check if DDD has callbacks for an Event class with
 * {@link DeveloperModeKernel#hasCallbacksForEvent(Class)}. While this just
 * checks if there are callbacks registered for that class, it is reasonable to
 * assume that if callbacks are registered for that class, DDD has a way to fire
 * them as either DDD fires the callbacks itself or something else (some mod
 * addon) does in the manner mentioned above.
 * 
 * @author Yeelp
 *
 */
@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DeveloperModeKernel {

	protected static enum DifficultyScaling {
		PEACEFUL(EnumDifficulty.PEACEFUL) {
			@Override
			protected float scaleDamage(float dmg) {
				return 0;
			}
		},
		EASY(EnumDifficulty.EASY) {
			@Override
			protected float scaleDamage(float dmg) {
				return Math.min(dmg / 2.0F + 1.0F, dmg);
			}
		},
		NONE(EnumDifficulty.NORMAL) {
			@Override
			protected float scaleDamage(float dmg) {
				return dmg;
			}
		},
		HARD(EnumDifficulty.HARD) {
			@Override
			protected float scaleDamage(float dmg) {
				return dmg * 3.0F / 2.0F;
			}
		};

		private final EnumDifficulty diff;

		private static final DifficultyScaling[] MAPPING = new DifficultyScaling[DifficultyScaling.values().length];

		protected abstract float scaleDamage(float dmg);

		final String getScaledDamage(float dmg) {
			if(this != NONE) {
				return String.format("(%.2f on %s)", this.scaleDamage(dmg), this.diff.toString());
			}
			return "";
		}

		private DifficultyScaling(EnumDifficulty diff) {
			this.diff = diff;
		}

		static DifficultyScaling getVanillaDifficultyScaling(DamageSource src, EntityLivingBase defender) {
			if(src.isDifficultyScaled() && defender instanceof EntityPlayer) {
				return MAPPING[defender.world.getDifficulty().getId()];
			}
			return NONE;
		}

		static {
			Arrays.stream(DifficultyScaling.values()).forEach((diff) -> MAPPING[diff.diff.getId()] = diff);
		}
	}

	private static final Logger LOGGER = LogManager.getLogger();
	private static DeveloperStatus currMode = DeveloperStatus.DISABLED;

	private static final Multimap<Class<? extends Event>, IDeveloperModeInfoCallback<? extends Event>> INFOS = MultimapBuilder.hashKeys().arrayListValues().build();

	public static void log(String msg, boolean inChat, Iterable<? extends EntityPlayer> players) {
		LOGGER.info("[DISTINCT DAMAGE DESCRIPTIONS (DEVELOPER)]: " + msg);
		if(inChat) {
			players.forEach((p) -> {
				if(p.world.isRemote) {
					return;
				}
				p.sendStatusMessage(new TextComponentString("[DDD DEVELOPER]: " + msg.replaceAll("\r", "")), false);
			});
		}
	}

	public static void logIfEnabled(String msg, boolean inChat, Iterable<? extends EntityPlayer> players) {
		if(ModConfig.dev.enabled) {
			log(msg, inChat, players);
		}
	}

	public static void log(String msg, Iterable<? extends EntityPlayer> players) {
		log(msg, currMode.sendInfoToChat(), players);
	}

	public static void log(String msg, EntityPlayer player) {
		log(msg, ImmutableList.of(player));
	}

	@Nonnull
	private static final <T, U> U mapIfNonNullElseGetDefault(T t, @Nonnull Function<T, U> f, @Nonnull U backup) {
		if(t != null) {
			return Objects.requireNonNull(f, "Function to apply can't be null!").apply(t);
		}
		return Objects.requireNonNull(backup, "backup can't be null!");
	}

	public static void initialize() {
		registerCallback(LivingAttackEvent.class, new LivingAttackEventInfo());
		registerCallback(LivingHurtEvent.class, new LivingHurtEventInfo());
		registerCallback(LivingDamageEvent.class, new LivingDamageEventInfo());
		registerCallback(DetermineDamageEvent.class, new DetermineDamageEventInfo());
		registerCallback(GatherDefensesEvent.class, new GatherDefensesEventInfo());
		registerCallback(ShieldBlockEvent.class, new ShieldBlockEventInfo());
		registerCallback(UpdateAdaptiveResistanceEvent.class, new UpdateAdaptiveResistanceEventInfo());
	}

	public static <E extends Event> boolean hasCallbacksForEvent(Class<E> clazz) {
		return INFOS.containsKey(clazz);
	}

	public static <E extends Event> void registerCallback(Class<E> clazz, IDeveloperModeInfoCallback<E> callback) {
		INFOS.put(clazz, callback);
	}

	@SuppressWarnings("unchecked")
	public static <E extends Event> void fireCallbacks(E evt) {
		if(!ModConfig.dev.enabled) {
			return;
		}
		if(FMLCommonHandler.instance().getSide().isServer()) {
			return;
		}
		Iterable<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
		INFOS.get(evt.getClass()).forEach((callback) -> {
			if((currMode = callback.getStatus()).isEnabled()) {
				IDeveloperModeInfoCallback<E> castedCallback = (IDeveloperModeInfoCallback<E>) callback;
				if(!castedCallback.getWorld(evt).isRemote && castedCallback.shouldFire(evt)) {
					log(castedCallback.callback(evt), players);
				}
			}
		});
	}

	@SubscribeEvent
	public static final void onPlayerJoin(PlayerLoggedInEvent evt) {
		logIfEnabled("DDD has loaded in Developer Mode! This is to assist in debugging information about damage calculations and custom damage types etc. Depending on your settings, your chat log and console log may be filled with lots of messages. It is recommended you use Developer Mode in blank void worlds to minimize chat spam!", true, ImmutableList.of(evt.player));
		if(ModConfig.dev.showConfigErrors) {
			Iterable<EntityPlayer> player = ImmutableList.of(evt.player);
			DDDConfigLoader.getErrorMessages().forEach((s) -> log(s, true, player));
		}
	}
}
