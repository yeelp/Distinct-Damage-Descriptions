package yeelp.distinctdamagedescriptions.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.CreatureType;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.FileHelper;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.NonNullMap;
import yeelp.distinctdamagedescriptions.util.SyntaxException;

public final class DDDCreatureTypeRegistries
{
	private static final Map<String, CreatureTypeData> CREATURE_TYPES = new NonNullMap<String, CreatureTypeData>(CreatureTypeData.UNKNOWN);
	private static final Map<String, Tuple<String, String>> CREATURE_MAP = new NonNullMap<String, Tuple<String, String>>(new Tuple<String, String>("unknown", "unknown"));
	private static File[] jsonFiles;
	private static File directory;
	
	public static void init()
	{
		directory = DistinctDamageDescriptions.getModConfigDirectory();
		if(directory.exists() || directory.mkdirs())
		{
			jsonFiles = directory.listFiles();
			if(writeExampleCreatureTypeJSON())
			{
				jsonFiles = directory.listFiles();
			}
			load();
			if(ModConfig.showDotsOn)
			{
				DistinctDamageDescriptions.debug("CREATURE TYPES:");
				for(CreatureTypeData data : CREATURE_TYPES.values())
				{
					DistinctDamageDescriptions.debug(data.getTypeName());
				}
			}
		}
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
				InputStream stream = DDDCreatureTypeRegistries.class.getClassLoader().getResourceAsStream(relativePath);
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
		try(FileInputStream inStream = new FileInputStream(json); BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF8")))
		{
			String firstLine = reader.readLine();
			return !("// Mod Version: "+ModConsts.VERSION).equals(firstLine);
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
	
	private static void load()
	{
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
							CREATURE_MAP.compute(j.getAsString(), (s, t) -> new Tuple<String, String>(type, t.getSecond()));
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
							CREATURE_MAP.compute(j.getAsString(), (s, t) -> new Tuple<String, String>(t.getFirst(), type));
						}
						else
						{
							throw new SyntaxException("Invalid Entity ID for sub type in JSON "+f.getName());
						}
					}
					MobResistanceCategories cats = new MobResistanceCategories(slash, pierce, bludge, slashImmune, pierceImmune, bludgeImmune, adaptiveChance, adaptiveAmount);
					CREATURE_TYPES.put(type, new CreatureTypeData(type, cats, potionImmunities));
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
	}
	
	private static float getJsonFloat(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsFloat();
	}
	
	private static String getJsonString(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsString();
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
	
	/**
	 * Get the CreatureTypeData(s) for a mob
	 * @param entity the entitylivingbase
	 * @return a Tuple of CreatureTypeData. If the first is CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	public static Tuple<CreatureTypeData, CreatureTypeData> getCreatureTypeForMob(EntityLivingBase entity)
	{
		String key = EntityList.getKey(entity).toString();
		Tuple<String, String> t = CREATURE_MAP.get(key);
		CreatureTypeData main = null, sub = null;
		if(!t.getFirst().equals(CreatureTypeData.UNKNOWN.getTypeName()))
		{
			main = CREATURE_TYPES.get(t.getFirst());
			if(!t.getSecond().equals(CreatureTypeData.UNKNOWN.getTypeName()))
			{
				sub = CREATURE_TYPES.get(t.getSecond());
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
	
	public static CreatureTypeData getCreatureTypeData(String key)
	{
		return CREATURE_TYPES.get(key);
	}
}
