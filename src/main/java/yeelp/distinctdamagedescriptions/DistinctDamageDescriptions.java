package yeelp.distinctdamagedescriptions;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.util.BaseResistances;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.ResistancesAttributes;

import java.util.Map;

import org.apache.logging.log4j.Logger;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    
    private static Map<String, BaseResistances> resistancesMap;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();  
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	DamageCategories.register();
        new DamageHandler().register();
        new ResistancesAttributes().register();
    }
    
    public static BaseResistances getResistances(String key)
    {
    	return resistancesMap.get(key);
    }
    public static void info(String msg)
    {
    	logger.info(msg);
    }
}
