package yeelp.distinctdamagedescriptions;

import net.minecraft.init.Blocks;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.util.BaseDamage;
import yeelp.distinctdamagedescriptions.util.BaseResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;
import yeelp.distinctdamagedescriptions.util.DDDAttributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    
    private static Map<String, Tuple<BaseDamage, BaseResistances>> propMap = new NonNullMap<String, Tuple<BaseDamage, BaseResistances>>(new Tuple(new BaseDamage(0.0f, 0.0f, 1.0f), new BaseResistances(0.0f, 0.0f, 0.0f)));

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog(); 
        DDDAPI.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new DamageHandler().register();
        new TooltipHandler().register();
        new DDDAttributes().register();
    }
    
    public static BaseResistances getResistances(String key)
    {
    	return propMap.get(key).getSecond();
    }
    
    public static BaseDamage getDamage(String key)
    {
    	return propMap.get(key).getFirst();
    }
    
    public static Tuple<BaseDamage, BaseResistances> getProperties(String key)
    {
    	return propMap.get(key);
    }
    
    public static void info(String msg)
    {
    	logger.info("[DISTINCT DAMAGE DESCRIPTIONS] "+msg);
    }
}
