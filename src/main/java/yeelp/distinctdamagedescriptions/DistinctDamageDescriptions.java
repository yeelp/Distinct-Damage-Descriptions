package yeelp.distinctdamagedescriptions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.CreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.DDDTrackers;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.MobHandler;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernel;
import yeelp.distinctdamagedescriptions.integration.tic.TiCHelper;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions {
	private static Logger logger;
	private static File configDirectory;
	private static Configuration config;

	@Instance(ModConsts.MODID)
	public static DistinctDamageDescriptions instance;
	public static File srcFile;

	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		configDirectory = event.getModConfigurationDirectory();
		config = new Configuration(event.getSuggestedConfigurationFile());
		srcFile = event.getSourceFile();
		DDDAPI.init();
		DDDRegistries.init();
		DDDConfigurations.init();
		DebugLib.updateStatus();
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void init(@SuppressWarnings("unused") FMLInitializationEvent event) {
		new DamageHandler().register();
		new TooltipHandler().register();
		new CapabilityHandler().register();
		new MobHandler().register();
		new TiCHelper().register();
		DDDTrackers.register();
		MobResistances.register();
		ArmorDistribution.register();
		DamageDistribution.register();
		CreatureType.register();
		ShieldDistribution.register();
		PacketHandler.init();
		DDDSounds.init();
		DDDEnchantments.init();

		new ModIntegrationKernel().load();
	}

	public static void info(String msg) {
		logger.info("[DISTINCT DAMAGE DESCRIPTIONS] " + msg);
	}

	public static void warn(String msg) {
		if(!ModConfig.suppressWarnings) {
			logger.warn("[DISTINCT DAMAGE DESCRIPTIONS] " + msg);
		}
	}

	public static void err(String msg) {
		logger.error("[DISTINCT DAMAGE DESCRIPTIONS] " + msg);
	}

	public static void fatal(String msg) {
		logger.fatal("[DISTINCT DAMAGE DESCRIPTIONS] " + msg);
	}

	public static void debug(String msg) {
		if(ModConfig.showDotsOn) {
			logger.info("[DISTINCT DAMAGE DESCRIPTIONS (DEBUG)]" + msg);
		}
	}

	public static File getModConfigDirectory() {
		return new File(configDirectory, ModConsts.MODID);
	}

	public static Configuration getConfiguration() {
		return config;
	}

}
