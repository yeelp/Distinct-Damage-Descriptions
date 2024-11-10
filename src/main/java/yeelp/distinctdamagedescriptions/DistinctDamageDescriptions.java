package yeelp.distinctdamagedescriptions;

import java.io.File;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.command.DDDCommand;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.MobHandler;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDInitialization;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernel;
import yeelp.distinctdamagedescriptions.items.DDDDiscItem;
import yeelp.distinctdamagedescriptions.proxy.Proxy;
import yeelp.distinctdamagedescriptions.util.json.DDDJsonIO;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION, certificateFingerprint = "176200587b5c58adc4f846bdcc0065ae8f1a3835")
public class DistinctDamageDescriptions {
	public static Logger logger;
	private static File configDirectory;
	private static Configuration config;

	@Instance(ModConsts.MODID)
	public static DistinctDamageDescriptions instance;
	public static File srcFile;

	@SidedProxy(clientSide = "yeelp.distinctdamagedescriptions.proxy.ClientProxy", serverSide = "yeelp.distinctdamagedescriptions.proxy.Proxy")
	public static Proxy proxy;
	
	private static final String LOGGER_REGULAR_PREFIX = String.format("[%s]", ModConsts.NAME.toUpperCase());
	private static final String LOGGER_DEBUG_PREFIX = String.format("[%s (DEBUG)]", ModConsts.NAME.toUpperCase());
	
	private static boolean fingerprintViolation = false;

	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		info(String.format("Distinct Damage Descriptions is version %s", ModConsts.VERSION));
		ModIntegrationKernel.load();
		configDirectory = event.getModConfigurationDirectory();
		config = new Configuration(event.getSuggestedConfigurationFile());
		srcFile = event.getSourceFile();
		if(fingerprintViolation) {
			proxy.handleFingerprintViolation();
		}
		DDDJsonIO.init();
		DDDInitialization.runLoaders(event);
		DebugLib.updateStatus();
		proxy.preInit();
		ModIntegrationKernel.doPreInit(event);
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModIntegrationKernel.doInitStart(event);
		DDDConfigLoader.readConfig();
		new CapabilityHandler().register();
		new TooltipHandler().register();
		new MobHandler().register();
		new DDDDiscItem.DropHandler().register();
		IMobResistances.register();
		IArmorDistribution.register();
		IDamageDistribution.register();
		IMobCreatureType.register();
		IDDDCombatTracker.register();
		ShieldDistribution.register();
		PacketHandler.init();
		DDDSounds.init();
		DDDEnchantments.init();
		ModIntegrationKernel.doInit(event);
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModIntegrationKernel.doPostInit(event);
	}
	
	@SuppressWarnings("static-method")
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new DDDCommand());
	}
	
	@SuppressWarnings("static-method")
	@EventHandler
	public void fingerprintViolation(FMLFingerprintViolationEvent event) {
		if(event.isDirectory()) {
			debug("Fingerprint doesn't matter in dev environment.");
		}
		else {
			fingerprintViolation = true;
		}
	}

	public static void info(String msg) {
		if(logger == null) {
			System.out.println(msg);
			return;
		}
		logNormal(msg, logger::info);
	}

	public static void warn(String msg) {
		if(logger == null) {
			System.out.println(msg);
			return;
		}
		if(!ModConfig.core.suppressWarnings) {
			logNormal(msg, logger::warn);
		}
	}

	public static void err(String msg) {
		if(logger == null) {
			System.err.println(msg);
			return;
		}
		logNormal(msg, logger::error);
	}

	public static void fatal(String msg) {
		if(logger == null) {
			System.err.println(msg);
			return;
		}
		logNormal(msg, logger::fatal);
	}

	public static void debug(String msg) {
		if(logger == null) {
			System.out.println(msg);
			return;
		}
		if(ModConfig.showDotsOn) {
			logger.info(String.format("%s %s", LOGGER_DEBUG_PREFIX, msg));
		}
	}
	
	private static void logNormal(String msg, Consumer<String> method) {
		method.accept(String.format("%s %s", LOGGER_REGULAR_PREFIX, msg));
	}

	public static File getModConfigDirectory() {
		return new File(configDirectory, ModConsts.MODID);
	}

	public static Configuration getConfiguration() {
		return config;
	}

}
