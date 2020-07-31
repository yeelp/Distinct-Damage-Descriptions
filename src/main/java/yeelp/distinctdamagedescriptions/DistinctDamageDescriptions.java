package yeelp.distinctdamagedescriptions;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.MobHandler;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorDistribution;
import yeelp.distinctdamagedescriptions.util.CreatureType;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.MobResistances;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    private static File configDirectory;
    
    @Instance(ModConsts.MODID)
    public static DistinctDamageDescriptions instance;
    public static File srcFile;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        configDirectory = event.getModConfigurationDirectory();
        srcFile = event.getSourceFile();
        DDDAPI.init();
        DDDRegistries.init();
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
        PacketHandler.init();
        DDDSounds.init();
        if(ModConfig.enableEnchants)
        {
        	DDDEnchantments.init();
        }
    }
    
    public static void info(String msg)
    {
    	logger.info("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    }
    
    public static void warn(String msg)
    {
    	logger.warn("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    }
    
    public static void err(String msg)
    {
    	logger.error("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
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
}
