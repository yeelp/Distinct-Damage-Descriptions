package yeelp.distinctdamagedescriptions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
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
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions {
	public static Logger logger;
	private static File configDirectory;
	private static Configuration config;

	@Instance(ModConsts.MODID)
	public static DistinctDamageDescriptions instance;
	public static File srcFile;

	@SidedProxy(clientSide = "yeelp.distinctdamagedescriptions.proxy.ClientProxy", serverSide = "yeelp.distinctdamagedescriptions.proxy.Proxy")
	public static Proxy proxy;

	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		ModIntegrationKernel.load();
		configDirectory = event.getModConfigurationDirectory();
		config = new Configuration(event.getSuggestedConfigurationFile());
		srcFile = event.getSourceFile();
		DDDInitialization.runLoaders(event);
		DebugLib.updateStatus();
		proxy.preInit();
		ModIntegrationKernel.doPreInit(event);
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void init(FMLInitializationEvent event) {
		DDDConfigLoader.readConfig();
		new CapabilityHandler().register();
		new TooltipHandler().register();
		new MobHandler().register();
		new DDDDiscItem.DropHandler().register();
		IMobResistances.register();
		IArmorDistribution.register();
		IDamageDistribution.register();
		ICreatureType.register();
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

	public static void info(String msg) {
		logger.info("[DISTINCT DAMAGE DESCRIPTIONS] " + msg);
	}

	public static void warn(String msg) {
		if(!ModConfig.core.suppressWarnings) {
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
