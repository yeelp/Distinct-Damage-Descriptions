package yeelp.distinctdamagedescriptions.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.ComparableTriple;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.FileHelper;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.NonNullMap;
import yeelp.distinctdamagedescriptions.util.SyntaxException;

public enum DDDRegistriesImpl implements IDDDCreatureTypeRegistry, IDDDMobResistancesRegistry, IDDDMobDamageRegistry, IDDDItemPropertiesRegistry, IDDDProjectilePropertiesRegistry
{
	INSTANCE;
	private final Map<String, MobResistanceCategories> mobResists = new NonNullMap<String, MobResistanceCategories>(new MobResistanceCategories(0, 0, 0, false, false, false, 0, 0));
	private final Map<String, CreatureTypeData> creatureTypes = new NonNullMap<String, CreatureTypeData>(CreatureTypeData.UNKNOWN);
	private final Map<String, Tuple<String, String>> creatureMap = new NonNullMap<String, Tuple<String, String>>(new Tuple<String, String>("unknown", "unknown"));
	private final Map<String, ComparableTriple<Float, Float, Float>> mobDamage = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
	private final Map<String, ComparableTriple<Float, Float, Float>> itemArmorDist = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 0.0f));
    private final Map<String, ComparableTriple<Float, Float, Float>> itemDamageDist = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
    private final Map<String, ComparableTriple<Float, Float, Float>> projectileDist = new NonNullMap<String, ComparableTriple<Float, Float, Float>>(new ComparableTriple<Float, Float, Float>(0.0f, 0.0f, 1.0f));
    private final Map<String, String> itemIDToProjIDMap = new HashMap<String, String>();
    
	private static File[] jsonFiles;
	private static File directory;
	DDDRegistriesImpl()
	{
		DDDRegistries.creatureTypes = this;
		DDDRegistries.mobResists = this;
		DDDRegistries.mobDamage = this;
		DDDRegistries.itemProperties = this;
		DDDRegistries.projectileProperties = this;
		init();
	}
	
	@Override
	public void init()
	{
		directory = DistinctDamageDescriptions.getModConfigDirectory();
		if(directory.exists() || directory.mkdirs())
		{
			jsonFiles = directory.listFiles();
			if(writeExampleCreatureTypeJSON())
			{
				jsonFiles = directory.listFiles();
			}
			DistinctDamageDescriptions.debug("Checked JSON");
			load();
			if(ModConfig.showDotsOn && ModConfig.resist.useCreatureTypes)
			{
				DistinctDamageDescriptions.debug("CREATURE TYPES:");
				for(CreatureTypeData data : creatureTypes.values())
				{
					DistinctDamageDescriptions.debug(data.getTypeName());
				}
			}
		}
	}	

	@Override
	public MobResistanceCategories getResistancesForMob(String key)
	{
		return mobResists.get(key);
	}

	@Override
	public Tuple<CreatureTypeData, CreatureTypeData> getCreatureTypeForMob(String key)
	{
		Tuple<String, String> t = creatureMap.get(key);
		CreatureTypeData main = null, sub = null;
		if(!t.getFirst().equals(CreatureTypeData.UNKNOWN.getTypeName()))
		{
			main = creatureTypes.get(t.getFirst());
			if(!t.getSecond().equals(CreatureTypeData.UNKNOWN.getTypeName()))
			{
				sub = creatureTypes.get(t.getSecond());
			}
			else
			{
				sub = CreatureTypeData.UNKNOWN;
			}
		}
		else
		{
			main = CreatureTypeData.UNKNOWN;
			sub = CreatureTypeData.UNKNOWN;
		}
		return new Tuple<CreatureTypeData, CreatureTypeData>(main, sub);
	}

	@Override
	public CreatureTypeData getCreatureTypeData(String key)
	{
		return creatureTypes.get(key);
	}
	
	@Override
	public ComparableTriple<Float, Float, Float> getMobDamage(String key)
	{
		return mobDamage.get(key);
	}
	
	@Override
	public ComparableTriple<Float, Float, Float> getDamageDistributionForItem(String key)
	{
		return itemDamageDist.get(key);
	}
	
	@Override 
	public ComparableTriple<Float, Float, Float> getArmorDistributionForItem(String key)
	{
		return itemArmorDist.get(key);
	}
	
	@Override
	public ComparableTriple<Float, Float, Float> getProjectileDamageTypes(String key)
    {
    	return projectileDist.get(key);
    }
    
	@Override
    public ComparableTriple<Float, Float, Float> getProjectileDamageTypesFromItemID(String itemID)
    {
    	String str = itemIDToProjIDMap.get(itemID);
    	if(str != null)
    	{
    		return projectileDist.get(str);
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
			DistinctDamageDescriptions.warn(s+" isn't a valid entry! Ignoring...");
		}
    	return null;
    }
	
	private static boolean writeExampleCreatureTypeJSON()
	{
		String name = "example_creature_type.json";
		String relativePath = "example/"+name;
		File dest = new File(directory, name);
		
		try
		{
			boolean shouldOverwrite = shouldOverwriteExampleJSON(dest);
			if(DistinctDamageDescriptions.srcFile != null && DistinctDamageDescriptions.srcFile.isDirectory())
			{
				File source = new File(DistinctDamageDescriptions.srcFile, relativePath);
				return FileHelper.copyFile(source, dest, shouldOverwrite);
			}
			else
			{
				InputStream stream = DDDRegistriesImpl.class.getClassLoader().getResourceAsStream(relativePath);
				boolean result = FileHelper.copyFile(stream, dest, shouldOverwrite);
				stream.close();
				return result;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean shouldOverwriteExampleJSON(File json)
	{
		boolean b = jsonFiles.length >= 1;
		boolean j = json.exists();
		try(FileInputStream inStream = new FileInputStream(json); BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF8")))
		{
			String firstLine = reader.readLine();
			return (j && !("// Mod Version: "+ModConsts.VERSION).equals(firstLine)) || (!j && !b);
		}
		catch (FileNotFoundException e)
		{
			return !b;
		}
		catch (IOException e)
		{
			DistinctDamageDescriptions.warn("Encountered a problem finding/parsing example creature type JSON. Will try to continue anyway...");
			return !b;
		}
	}
	
	private void load()
	{
		//CREATURE TYPES FROM JSON
		if(ModConfig.resist.useCreatureTypes)
		{
			DistinctDamageDescriptions.info("Creature Types Enabled!");
			JsonParser parser = new JsonParser();
			for(File f : jsonFiles)
			{
				if(FilenameUtils.getExtension(f.getName()).equalsIgnoreCase(".json"))
				{
					continue;
				}
				try
				{
					JsonReader reader = new JsonReader(new FileReader(f));
					reader.setLenient(true);
					JsonElement elem = parser.parse(reader);
					JsonObject obj = elem.getAsJsonObject();
					String type = obj.get("name").getAsString().toLowerCase();
					if(type.equals("unknown"))
					{
						throw new IllegalArgumentException("unknown is an invaild creature type!");
					}
					else
					{
						float slash = getJsonFloat(obj, "slashing_resistance", f);
						float pierce = getJsonFloat(obj, "piercing_resistance", f);
						float bludge = getJsonFloat(obj, "bludgeoning_resistance", f);
						String immunities = getJsonString(obj, "immunities", f);
						boolean slashImmune = immunities.contains("s");
						boolean pierceImmune = immunities.contains("p");
						boolean bludgeImmune = immunities.contains("b");
						float adaptiveChance = getJsonFloat(obj, "adaptability_chance", f);
						float adaptiveAmount = getJsonFloat(obj, "adaptability_amount", f);
						boolean critImmunity = getJsonBoolean(obj, "critical_hit_immunity", f);
						//We use a HashSet as that most likely guarantees a fast containment check.
						HashSet<String> potionImmunities = new HashSet<String>();
						for(JsonElement j : getJsonArray(obj, "potion_immunities", f))
						{
							if(j.isJsonPrimitive() && j.getAsJsonPrimitive().isString())
							{
								potionImmunities.add(j.getAsString());
							}
							else
							{
								throw new SyntaxException("Invalid potion immunity in JSON "+f.getName());
							}
						}
						//Update map with new info
						for(JsonElement j : getJsonArray(obj, "main_type_mobs", f))
						{
							if(j.isJsonPrimitive() && j.getAsJsonPrimitive().isString())
							{
								creatureMap.compute(j.getAsString(), (s, t) -> new Tuple<String, String>(type, t.getSecond()));
							}
							else
							{
								throw new SyntaxException("Invalid Entity ID for main type in JSON "+f.getName());
							}
						}
						for(JsonElement j : getJsonArray(obj, "sub_type_mobs", f))
						{
							if(j.isJsonPrimitive() && j.getAsJsonPrimitive().isString())
							{
								creatureMap.compute(j.getAsString(), (s, t) -> new Tuple<String, String>(t.getFirst(), type));
							}
							else
							{
								throw new SyntaxException("Invalid Entity ID for sub type in JSON "+f.getName());
							}
						}
						MobResistanceCategories cats = new MobResistanceCategories(slash, pierce, bludge, slashImmune, pierceImmune, bludgeImmune, adaptiveChance, adaptiveAmount);
						creatureTypes.put(type, new CreatureTypeData(type, cats, potionImmunities, critImmunity));
					}
				}
				catch(FileNotFoundException e)
				{
					DistinctDamageDescriptions.err("Could not find JSON!");
				}
				catch(IllegalStateException | ClassCastException e1)
				{
					DistinctDamageDescriptions.err("Could not parse "+f.getName()+" as a CreatureType!");
				}
			}
			for(String s : creatureMap.keySet())
			{
				Tuple<CreatureTypeData, CreatureTypeData> data = getCreatureTypeForMob(s);
				CreatureTypeData main = data.getFirst(), sub = data.getSecond();
				if(main == CreatureTypeData.UNKNOWN)
				{
					continue;
				}
				else
				{
					if(sub == CreatureTypeData.UNKNOWN)
					{
						mobResists.put(s, main.getMobResistances());
					}
					else
					{
						MobResistanceCategories mainCat = main.getMobResistances(), subCat = sub.getMobResistances();
						MobResistanceCategories cats = new MobResistanceCategories(0.75f*mainCat.getSlashingResistance() + 0.25f*subCat.getSlashingResistance(), 0.75f*mainCat.getPiercingResistance() + 0.25f*subCat.getPiercingResistance(), 0.75f*mainCat.getBludgeoningResistance() + 0.25f*subCat.getBludgeoningResistance(), mainCat.getSlashingImmunity(), mainCat.getPiercingImmunity(), mainCat.getBludgeoningImmunity(), mainCat.adaptiveChance(), mainCat.getAdaptiveAmount());
						mobResists.put(s, cats);
					}
				}
			}
			DistinctDamageDescriptions.info("Loaded Creature Types!");
		}
		//MOB RESISTANCES FROM CONFIG
		for(String s : ModConfig.resist.mobBaseResist)
		{
			try
			{
				String[] contents = s.split(";");
				mobResists.put(contents[0], new MobResistanceCategories(Float.valueOf(contents[1]), Float.valueOf(contents[2]), Float.valueOf(contents[3]), contents[4].contains("s"), contents[4].contains("p"), contents[4].contains("b"), Float.valueOf(contents[5]), Float.valueOf(contents[6])));
			}
			catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
			{
				DistinctDamageDescriptions.warn(s+" isn't a valid entry! Ignoring...");
			}
		}
		DistinctDamageDescriptions.info("Mob resistances from config loaded!");
		//MOB DAMAGE FROM CONFIG
		for(String s : ModConfig.dmg.mobBaseDmg)
		{
			tryPut(mobDamage, s);
		}
		DistinctDamageDescriptions.info("Mob damage loaded!");
		//ARMOR RESISTANCES FROM CONFIG
		for(String s : ModConfig.resist.armorResist)
		{
			tryPut(itemArmorDist, s);
		}
		DistinctDamageDescriptions.info("Armor resistances loaded!");
		//WEAPON DAMAGE FROM CONFIG
		for(String s : ModConfig.dmg.itemBaseDamage)
		{
			tryPut(itemDamageDist, s);
		}
		DistinctDamageDescriptions.info("Weapon damage loaded!");
		//Projectile Damage Types from Config
		for(String s : ModConfig.dmg.projectileDamageTypes)
		{
			String[] entry = tryPut(projectileDist, s);
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
					DistinctDamageDescriptions.warn("Expected projectile item forms for "+entry[0]+" but found none! Either remove the trailing semicolon from this entry, or add item id's!");
				}
			}
		}
		DistinctDamageDescriptions.info("Projectile damage loaded!");
	}
	
	private static float getJsonFloat(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsFloat();
	}
	
	private static String getJsonString(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsString();
	}
	
	private static boolean getJsonBoolean(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsBoolean();
	}
	
	private static JsonArray getJsonArray(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsJsonArray();
	}
	
	@Nonnull
	private static JsonElement getJsonElement(JsonObject obj, String memberName, File f)
	{
		JsonElement e = obj.get(memberName);
		if(e == null)
		{
			throw new SyntaxException(memberName+" not found in JSON file "+f.getName()+"! Verify it's present and spelled correctly.");
		}
		else
		{
			return e;
		}
	}
}
