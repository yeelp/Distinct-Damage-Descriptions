package yeelp.distinctdamagedescriptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.PacketHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.util.ArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ComparableTriple;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.MobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    
    private static Map<String, MobResistanceCategories> resistMap = new NonNullMap<String, MobResistanceCategories>(new MobResistanceCategories(0.0f, 0.0f, 0.0f, false, false, false, 0.0f));
    private static Map<String, ComparableTriple<Float, Float, Float>> damageMap = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
    private static Map<String, ComparableTriple<Float, Float, Float>> armorMap = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 0.0f));
    private static Map<String, ComparableTriple<Float, Float, Float>> weaponMap = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
    private static Map<String, ComparableTriple<Float, Float, Float>> projectileMap = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
    private static Map<String, String> itemIDToProjIDMap = new HashMap<String, String>();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog(); 
        DDDAPI.init();
        populateMaps();
    }

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
        new DamageHandler().register();
        new TooltipHandler().register();
        new CapabilityHandler().register();
        MobResistances.register();
        ArmorDistribution.register();
        DamageDistribution.register();
        PacketHandler.init();
    }
	
    private void populateMaps()
	{
    	//Mob Resistances
		for(String s : ModConfig.resist.mobBaseResist)
		{
			try
			{
				String[] contents = s.split(";");
				resistMap.put(contents[0], new MobResistanceCategories(Float.valueOf(contents[1]), Float.valueOf(contents[2]), Float.valueOf(contents[3]), contents[4].contains("s"), contents[4].contains("p"), contents[4].contains("b"), Float.valueOf(contents[5])));
			}
			catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
			{
				warn(s+" isn't a valid entry! Ignoring...");
			}
		}
		info("Mob resistances loaded!");
		//Mob Damage
		for(String s : ModConfig.dmg.mobBaseDmg)
		{
			tryPut(damageMap, s);
		}
		info("Mob damage loaded!");
		//Armor Resistances
		for(String s : ModConfig.resist.armorResist)
		{
			tryPut(armorMap, s);
		}
		info("Armor resistances loaded!");
		//Weapon Damage
		for(String s : ModConfig.dmg.itemBaseDamage)
		{
			tryPut(weaponMap, s);
		}
		info("Weapon damage loaded!");
		//Projectile Damage Types
		for(String s : ModConfig.dmg.projectileDamageTypes)
		{
			String[] entry = tryPut(projectileMap, s);
			if(entry.length == 5)
			{
				if(!entry[4].trim().isEmpty())
				{
					for(String i : entry[4].split(","))
					{
						itemIDToProjIDMap.put(i, entry[0]);
					}
				}
				else
				{
					warn("Expected projectile item forms for "+entry[0]+" but found none! Either remove the trailing semicolon from this entry, or add item id's!");
				}
			}
		}
		info("Projectile damage loaded!");
	}
    
    @Nonnull
    public static MobResistanceCategories getMobResistances(String key)
    {
    	return resistMap.get(key);
    }
    
    @Nonnull
    public static ComparableTriple<Float, Float, Float> getMobDamage(String key)
    {
    	return damageMap.get(key);
    }
    
    @Nonnull
    public static ComparableTriple<Float, Float, Float> getArmorDistribution(String key)
    {
    	return armorMap.get(key);
    }
    
    @Nonnull
    public static ComparableTriple<Float, Float, Float> getWeaponDamage(String key)
    {
    	return weaponMap.get(key);
    }
    
    @Nonnull
    public static ComparableTriple<Float, Float, Float> getProjectileDamageTypes(String key)
    {
    	return projectileMap.get(key);
    }
    
    @Nullable
    public static ComparableTriple<Float, Float, Float> getProjectileDamageTypesFromItemID(String itemID)
    {
    	String str = itemIDToProjIDMap.get(itemID);
    	if(str != null)
    	{
    		return projectileMap.get(str);
    	}
    	else
    	{
    		return null;
    	}
    }
    private static String[] tryPut(Map<String, ComparableTriple<Float, Float, Float>> map, String s)
    {
    	String[] contents = s.split(";");
    	try
		{
			map.put(contents[0], new ComparableTriple<Float, Float, Float>(Float.valueOf(contents[1]), Float.valueOf(contents[2]), Float.valueOf(contents[3])));
			return contents;
		}
		catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			warn(s+" isn't a valid entry! Ignoring...");
		}
    	return null;
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
}
