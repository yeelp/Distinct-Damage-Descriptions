package yeelp.distinctdamagedescriptions;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.*;
import yeelp.distinctdamagedescriptions.handlers.*;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernel;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

import java.io.File;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    private static File configDirectory;
    private static Configuration config;
    
    @Instance(ModConsts.MODID)
    public static DistinctDamageDescriptions instance;
    public static File srcFile;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        configDirectory = event.getModConfigurationDirectory();
        config = new Configuration(event.getSuggestedConfigurationFile());
        srcFile = event.getSourceFile();
        DDDAPI.init();
        DDDRegistries.init();
        DebugLib.updateStatus();
    }

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
        new DamageHandler().register();
        new TooltipHandler().register();
        new CapabilityHandler().register();
        new MobHandler().register();
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
    
    public static void info(String msg)
    {
    	logger.info("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    }
    
    public static void warn(String msg)
    {
    	if(!ModConfig.suppressWarnings)
    	{
    		logger.warn("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    	}
    }
    
    public static void err(String msg)
    {
    	logger.error("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    }
    
    public static void fatal(String msg)
	{
		logger.fatal("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
	}
    
	public static void debug(String msg)
	{
		if(ModConfig.showDotsOn)
		{
			logger.info("[DISTINCT DAMAGE DESCRIPTIONS (DEBUG)]" + msg);
		}
	}
	
	public static File getModConfigDirectory()
	{
		return new File(configDirectory, ModConsts.MODID);
	}
	
	public static Configuration getConfiguration()
	{
		return config;
	}
	
}
