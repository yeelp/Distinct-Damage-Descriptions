package yeelp.distinctdamagedescriptions.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.capability.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.init.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

/**
 * Read configuration entries from Config
 * @author Yeelp
 *
 */
public final class DDDConfigReader
{
	public static void readConfig()
	{
		readBasicConfigEntry(ModConfig.dmg.mobBaseDmg, 0.0f, DDDConfigurations.mobDamage, (map -> new DamageDistribution(map)));
		DistinctDamageDescriptions.info("Mob damage loaded!");
		readBasicConfigEntry(ModConfig.dmg.itemBaseDamage, 0.0f, DDDConfigurations.items, (map -> new DamageDistribution(map)));
		DistinctDamageDescriptions.info("Weapon damage loaded!");
		readBasicConfigEntry(ModConfig.resist.armorResist, 0.0f, DDDConfigurations.armors, (map -> new ArmorDistribution(map)));
		DistinctDamageDescriptions.info("Armor Resistances loaded!");
		readBasicConfigEntry(ModConfig.resist.shieldResist, 0.0f, DDDConfigurations.shields, (map -> new ShieldDistribution(map)));
		DistinctDamageDescriptions.info("Shield Distributions loaded!");
		readMobResistances();
		DistinctDamageDescriptions.info("Mob Resistances loaded!");
		readProjectileDamage();
		DistinctDamageDescriptions.info("Projectile Damage loaded!");
	}
	private static void readMobResistances()
	{
		for(String s : ModConfig.resist.mobBaseResist)
		{
			if(s.endsWith(";"))
			{
				DistinctDamageDescriptions.err("Config entry "+s+" shouldn't end in a semicolon! Please remove!");
				continue;
			}
			else
			{
				String[] arr = s.split(";");
				try
				{
					NonNullMap<DDDDamageType, Float> map = buildMap(0.0f, parseListOfTuples(arr[1]));
					Set<DDDDamageType> immunities = parseImmunitiesFromArray(arr[2]);
					DDDConfigurations.mobResists.put(arr[0], new MobResistanceCategories(map, immunities, Float.parseFloat(arr[2]), Float.parseFloat(arr[3])));
				}
				catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
				{
					DistinctDamageDescriptions.warn(s+" isn't a valid entry! Ignoring...");
				}
			}
		}
	}
	
	private static void readProjectileDamage()
	{
		for(String s : ModConfig.dmg.projectileDamageTypes)
		{
			String[] arr = tryPut(0.0f, s, DDDConfigurations.projectiles, (map -> new DamageDistribution(map)));
			if(arr == null || arr.length == 2)
			{
				continue;
			}
			else
			{
				for(String str : arr[3].split(","))
				{
					DDDConfigurations.projectiles.registerItemProjectilePair(str.trim(), arr[0]);
				}
			}
		}
	}
	
	private static <T> void readBasicConfigEntry(String[] arr, float defaultVal, IDDDConfiguration<T> config, Function<NonNullMap<DDDDamageType, Float>, T> constructor)
	{
		for(String s : arr)
		{
			tryPut(defaultVal, s, config, constructor);
		}
	}
	
	private static <T> String[] tryPut(float defaultVal, String s, IDDDConfiguration<T> config, Function<NonNullMap<DDDDamageType, Float>, T> constructor)
    {
		if(s.endsWith(";"))
		{
			DistinctDamageDescriptions.err("Config entry "+s+" shouldn't end in a semicolon! Please remove!");
			return null;
		}
		String[] contents = s.split(";");
    	try
		{
    		config.put(contents[0], constructor.apply(buildMap(defaultVal, parseListOfTuples(contents[1]))));
			return contents;
		}
		catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			DistinctDamageDescriptions.warn(s+" isn't a valid entry! Ignoring...");
		}
    	return null;
    }
	
	private static Iterable<Tuple<DDDDamageType, Float>> parseListOfTuples(String s)
	{
		//s is of the form [(t, a), (t, a), ... , (t, a)]
		if(s.equals("[]"))
		{
			return Collections.emptyList();
		}
		List<Tuple<DDDDamageType, Float>> lst = new LinkedList<Tuple<DDDDamageType, Float>>();
		for(String str : s.substring(2, s.length() - 2).split("\\),(?:\\s?)\\("))
		{
			String[] temp = s.split(",");
			lst.add(new Tuple<DDDDamageType, Float>(DDDRegistries.damageTypes.get(temp[0].trim()), Float.parseFloat(temp[1].trim())));
		}
		return lst;
	}
	
	private static <K, V> NonNullMap<K, V> buildMap(V defaultVal, Iterable<Tuple<K, V>> mappings)
	{
		if(mappings == null)
		{
			return new NonNullMap<K, V>(defaultVal);
		}
		else
		{
			NonNullMap<K, V> map = new NonNullMap<K, V>(defaultVal);
			for(Tuple<K, V> t : mappings)
			{
				map.put(t.getFirst(), t.getSecond());
			}
			return map;
		}
	}
	
	private static Set<DDDDamageType> parseImmunitiesFromArray(String s)
	{
		Set<DDDDamageType> set = new HashSet<DDDDamageType>();
		if(s.equals("") || s.equals("[]"))
		{
			return set;
		}
		else
		{
			String[] arr = s.substring(1, s.length() - 1).split(",");
			for(String str : arr)
			{
				set.add(DDDRegistries.damageTypes.get(str.trim()));
			}
			return set;
		}
	}
}
