package yeelp.distinctdamagedescriptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.handlers.DamageHandler;
import yeelp.distinctdamagedescriptions.handlers.TooltipHandler;
import yeelp.distinctdamagedescriptions.util.DamageCategories;
import yeelp.distinctdamagedescriptions.util.DamageDistribution;
import yeelp.distinctdamagedescriptions.util.ResistanceCategories;
import yeelp.distinctdamagedescriptions.util.ArmorResistances;
import yeelp.distinctdamagedescriptions.util.DamageType;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.MobResistances;
import yeelp.distinctdamagedescriptions.util.NonNullMap;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public class DistinctDamageDescriptions
{
    private static Logger logger;
    
    private static Map<String, MobResistanceCategories> resistMap = new NonNullMap<String, MobResistanceCategories>(new MobResistanceCategories(0.0f, 0.0f, 0.0f, false, false, false, false));
    private static Map<String, DamageCategories> damageMap = new HashMap<String, DamageCategories>();
    private static Map<String, ArmorResistanceCategories> armorMap = new NonNullMap<String, ArmorResistanceCategories>(new ArmorResistanceCategories(0.0f, 0.0f, 0.0f, false, false, false, 0.0f));
    private static Map<String, DamageCategories> weaponMap = new NonNullMap<String, DamageCategories>(new DamageCategories(0.0f, 0.0f, 0.0f));
    private static Map<String, Set<DamageType>> projectileMap = new NonNullMap<String, Set<DamageType>>(new HashSet<DamageType>(Lists.asList(DamageType.PIERCING, new DamageType[] {})));
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog(); 
        DDDAPI.init();
        //populateMaps();
    }

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
        new DamageHandler().register();
        new TooltipHandler().register();
        new CapabilityHandler().register();
        MobResistances.register();
        ArmorResistances.register();
        DamageDistribution.register();
    }
	
    /*private void populateMaps()
	{
    	//Mob Resistances
		for(String s : ModConfig.resist.mobBaseResist)
		{
			tryPut(resistMap, s, ResistanceCategories.class);
		}
		//Mob Damage
		for(String s : ModConfig.dmg.mobBaseDmg)
		{
			tryPut(damageMap, s, DamageCategories.class);
		}
		//Armor Resistances
		for(String s : ModConfig.resist.armorResist)
		{
			tryPut(armorMap, s, ResistanceCategories.class);
		}
		//Weapon Damage
		for(String s : ModConfig.dmg.itemBaseDamage)
		{
			tryPut(weaponMap, s, DamageCategories.class);
		}
		//Projectile Damage Types
		//We don't use the tryPut method, ad this was only for convenience for the first four
		//projectile damage types are handled a little differently.
		for(String s : ModConfig.dmg.projectileDamageTypes)
		{
			String[] contents = s.split(";");
			HashSet<DamageType> set = new HashSet<DamageType>();
			try
			{
				for(char c : contents[1].toCharArray())
				{
					set.add(DamageType.parseDamageType(c));
				}
			}
			catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e)
			{
				warn(s+" isn't a valid projectile entry! Ignoring...");
			}
		}
	}*/
    
    @Nonnull
    public static MobResistanceCategories getMobResistances(String key)
    {
    	return resistMap.get(key);
    }
    
    @Nullable
    public static DamageCategories getMobDamage(String key)
    {
    	return damageMap.get(key);
    }
    
    @Nonnull
    public static ArmorResistanceCategories getArmorResist(String key)
    {
    	return armorMap.get(key);
    }
    
    @Nonnull
    public static DamageCategories getWeaponDamage(String key)
    {
    	return weaponMap.get(key);
    }
    
    @Nonnull
    public static Set<DamageType> getProjectileDamageTypes(String key)
    {
    	return projectileMap.get(key);
    }
    
    private static<T> void tryPut(Map<String, T> map, String s, Class<T> clazz)
    {
    	String[] contents = s.split(";");
    	try
		{
    		Constructor<T> construct = clazz.getConstructor(float.class, float.class, float.class);
    		T val = construct.newInstance(Float.valueOf(contents[1]), Float.valueOf(contents[2]), Float.valueOf(contents[3]));
			map.put(contents[0], val);
		}
		catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			warn(s+" isn't a valid entry! Ignoring...");
		}
    	catch(InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException f)
    	{
    		err("Encountered a problem reading: "+s+", this doesn't make sense! Report to the mod author on GitHub! Provide this stack trace:");
    		err(Arrays.toString(f.getStackTrace()));
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
}
