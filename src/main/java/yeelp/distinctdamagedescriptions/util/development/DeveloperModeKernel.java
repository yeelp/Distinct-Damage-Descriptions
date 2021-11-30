package yeelp.distinctdamagedescriptions.util.development;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatTracker;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DeveloperModeKernel {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String NEW_LINE = System.lineSeparator();
	private static DeveloperStatus currMode = DeveloperStatus.DISABLED;
	private static final Map<Class<? extends Event>, Supplier<DeveloperStatus>> statusMap = ImmutableMap.<Class<? extends Event>, Supplier<DeveloperStatus>>builder()
			.put(LivingAttackEvent.class, () -> ModConfig.dev.showAttackInfo)
			.put(LivingHurtEvent.class, () -> ModConfig.dev.showHurtInfo)
			.put(LivingDamageEvent.class, () -> ModConfig.dev.showDamageInfo)
			.put(DetermineDamageEvent.class, () -> ModConfig.dev.showDamageClassification)
			.put(GatherDefensesEvent.class, () -> ModConfig.dev.showDefenseClassification)
			.put(ShieldBlockEvent.class, () -> ModConfig.dev.showShieldCalculation)
			.put(UpdateAdaptiveResistanceEvent.class, () -> ModConfig.dev.showAdaptiveCalculation)
			.build();

	public static void log(String msg, boolean inChat, Iterable<? extends EntityPlayer> players) {
		LOGGER.info("[DISTINCT DAMAGE DESCRIPTIONS (DEVELOPER)]: " + msg);
		if(inChat) {
			players.forEach((p) -> p.sendStatusMessage(new TextComponentString("[DDD DEVELOPER]: " + msg.replaceAll("\r", "")), false));
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
	
	private static final <T> void doIfEnabled(@Nonnull T t, @Nonnull BiFunction<T, StringBuilder, StringBuilder> log) {
		if(ModConfig.dev.enabled && ((currMode = statusMap.get(t.getClass()).get()).isEnabled())) {
			log(Objects.requireNonNull(log, "action can't be null!").andThen(Functions.toStringFunction()).apply(Objects.requireNonNull(t, "argument to action can't be null"), new StringBuilder()), FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers());			
		}
	}
	
	private static final String getEntityNameAndID(@Nonnull Entity entity) {
		return String.format("%s (%s)", entity.getName(), YResources.getEntityIDString(entity).orElse("No Entity ID found!"));
	}

	public static final void onAttackCallback(LivingAttackEvent evt) {
		if(evt.getSource().getImmediateSource() == null && evt.getSource().getTrueSource() == null && evt.getSource().damageType.equals("generic") && evt.getAmount() == 0) {
			return;
		}
		doIfEnabled(evt, (e, sb) -> {
			DamageSource src = e.getSource();
			String direct = mapIfNonNullElseGetDefault(src.getImmediateSource(), DeveloperModeKernel::getEntityNameAndID, "None");
			String indirect = mapIfNonNullElseGetDefault(src.getTrueSource(), DeveloperModeKernel::getEntityNameAndID, "None");
			sb.append(String.format("ATTACK: Direct Attacker: %s | Attacker: %s | Defender: %s | Source: %s | Current Damage: %.2f", direct, indirect, e.getEntityLiving().getName(), src.damageType, e.getAmount()));
			return sb;
		});
	}

	public static final void onHurtCallback(LivingHurtEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			DDDAPI.accessor.getDDDCombatTracker(e.getEntityLiving()).ifPresent((tracker) -> {
				AbstractAttributeMap attributes = tracker.getFighter().getAttributeMap();
				float armor = mapIfNonNullElseGetDefault(attributes.getAttributeInstance(SharedMonsterAttributes.ARMOR).getModifier(DDDCombatTracker.ARMOR_CALC_UUID), AttributeModifier::getAmount, 0).floatValue();
				float toughness = mapIfNonNullElseGetDefault(attributes.getAttributeInstance(SharedMonsterAttributes.ARMOR_TOUGHNESS).getModifier(DDDCombatTracker.TOUGHNESS_CALC_UUID), AttributeModifier::getAmount, 0).floatValue();
				if(armor != 0 || toughness != 0) {
					sb.append(String.format("HURT: Defender %s got a %+.2f armor and %+.2f toughness modification from armor effectiveness.", tracker.getFighter().getName(), armor, toughness));
					sb.append(NEW_LINE);
				}
				sb.append(String.format("Current damage: %.2f", e.getAmount()));
			});
			return sb;
		});
	}

	public static final void onDamageCallback(LivingDamageEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			sb.append(String.format("DAMAGE: Final damage amount for %s after resistances/immunities: %.2f", e.getEntityLiving().getName(), e.getAmount()));
			return sb;
		});
	}

	public static final void onDetermineDamageCallback(DetermineDamageEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			sb.append("DETERMINE DAMAGE: Damage Classification").append(NEW_LINE);
			DDDRegistries.damageTypes.getAll().stream().filter((type) -> e.getDamage(type) > 0).map((type) -> String.format("%s: %.2f%n", type.getDisplayName(), e.getDamage(type))).forEach(sb::append);
			return sb;
		});
	}

	public static final void onGatherDefensesCallback(GatherDefensesEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			sb.append("GATHER DEFENSES: Defense Classification").append(NEW_LINE);
			DDDRegistries.damageTypes.getAll().stream().flatMap((type) -> {
				Stream.Builder<String> stream = Stream.builder();
				if(e.hasResistance(type)) {
					stream.accept(String.format("%s Resistance: %.2f%%", type.getDisplayName(), e.getResistance(type)*100));
					stream.accept(e.hasImmunity(type) ? " | " : NEW_LINE);
				}
				if(e.hasImmunity(type)) {
					stream.accept(String.format("%s Immunity", type.getDisplayName()));
					stream.accept(NEW_LINE);
				}
				return stream.build();
			}).forEach(sb::append);
			return sb;
		});
	}

	public static final void onShieldBlockCallback(ShieldBlockEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			sb.append("SHIELD: ");
			if(e.isCanceled()) {
				sb.append(String.format("Shield blocking disabled for %s", e.getDefender().getName()));
			}
			else {
				sb.append("Shield Distribution").append(NEW_LINE);
				DDDRegistries.damageTypes.getAll().stream().filter((type) -> e.getShieldDistribution().getWeight(type) > 0).map((type) -> String.format("%s: %.2f%n", type.getDisplayName(), e.getShieldDistribution().getWeight(type))).forEach(sb::append);
			}
			return sb;
		});
	}

	public static final void onUpdateAdaptabilityCallback(UpdateAdaptiveResistanceEvent evt) {
		doIfEnabled(evt, (e, sb) -> {
			sb.append("ADAPTIVE: ");
			switch(e.getResult()) {
				case DEFAULT:
					if(!DDDAPI.accessor.getMobResistances(e.getDefender()).filter(IMobResistances::hasAdaptiveResistance).isPresent()) {
						sb.append(String.format("Defender %s isn't adaptive, and won't adapt.", e.getDefender().getName()));
						break;
					}
				case ALLOW:
					sb.append(String.format("Adaptability allowed for %s", e.getDefender().getName())).append(NEW_LINE).append("Adapting to types: ");
					sb.append(YLib.joinNiceString(true, ",", e.getDamageToAdaptTo().keySet().stream().map(DDDDamageType::getDisplayName).toArray(String[]::new)));
					break;
				default:
					sb.append(String.format("Adaptability disallowed for %s", e.getDefender().getName()));
					break;
			}
			return sb;
		});
	}
	
	@SubscribeEvent
	public static final void onPlayerJoin(PlayerLoggedInEvent evt) {
		logIfEnabled("DDD has loaded in Developer Mode! This is to assist in debugging information about damage calculations and custom damage types etc. Depending on your settings, your chat log and console log may be filled with lots of messages. It is recommended you use Developer Mode in blank void worlds to minimize chat spam!", true, ImmutableList.of(evt.player));
	}
}
