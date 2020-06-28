package yeelp.distinctdamagedescriptions;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.util.BaseResistances;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.ResistancesAttributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    
    private static Map<String, BaseResistances> resistancesMap = new HashMap<String, BaseResistances>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog(); 
        DDDAPI.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	DamageCategories.register();
        new DamageHandler().register();
        new CapabilityHandler().register();
        new TooltipHandler().register();
        new ResistancesAttributes().register();
    }
    
    public static BaseResistances getResistances(String key)
    {
    	return null;//resistancesMap.get(key);
    }
    public static void info(String msg)
    {
    	logger.info(msg);
    }
}
