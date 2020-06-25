package yeelp.distinctdamagedescriptions;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;

import org.apache.logging.log4j.Logger;

@Mod(modid = DistinctDamageDescriptions.MODID, name = DistinctDamageDescriptions.NAME, version = DistinctDamageDescriptions.VERSION)
public class DistinctDamageDescriptions
{
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();  
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new DamageHandler().register();
    }
    
    public static void info(String msg)
    {
    	logger.info(msg);
    }
}
