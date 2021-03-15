package yeelp.distinctdamagedescriptions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDCustomDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDCustomDistributions;
import yeelp.distinctdamagedescriptions.util.lib.FileHelper;
import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

/**
 * Handles all of DDD's JSON IO
 * @author Yeelp
 *
 */
public final class DDDJsonIO
{
	private static File[] creatureJsonFiles, damageTypeJsonFiles;
	private static File creatureDirectory, damageTypeDirectory;
	
	public static DDDCustomDistributions init()
	{
		File mainDirectory = DistinctDamageDescriptions.getModConfigDirectory();
		creatureDirectory = new File(mainDirectory, "creatureTypes");
		damageTypeDirectory = new File(mainDirectory, "damageTypes");
		checkJSON();
		return loadFromJSON();
		
	}
	
	private static void checkJSON()
	{
		if((creatureDirectory.exists() && damageTypeDirectory.exists()) || (damageTypeDirectory.mkdirs() && creatureDirectory.mkdirs()))
		{
			creatureJsonFiles = creatureDirectory.listFiles();
			damageTypeJsonFiles = damageTypeDirectory.listFiles();
			if(writeExampleJSON("example_creature_type.json", creatureDirectory))
			{
				creatureJsonFiles = creatureDirectory.listFiles();
			}
			if(writeExampleJSON("example_damage_type.json", damageTypeDirectory))
			{
				damageTypeJsonFiles = damageTypeDirectory.listFiles();
			}
			DistinctDamageDescriptions.debug("Checked JSON");
		}
	}
	
	private static boolean writeExampleJSON(String filename, File parentDirectory)
	{
		if(ModConfig.generateJSON)
		{
			String relativePath = "example/"+filename;
			File dest = new File(parentDirectory, filename);	
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
					InputStream stream = DDDJsonIO.class.getClassLoader().getResourceAsStream(relativePath);
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
		else
		{
			return false;
		}
	}
	
	private static boolean shouldOverwriteExampleJSON(File json)
	{
		boolean b = json.getParentFile().listFiles().length >= 1;
		boolean j = json.exists();
		try(FileInputStream inStream = new FileInputStream(json); BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF8")))
		{
			String firstLine = reader.readLine();
			return ((j && !("// Mod Version: "+ModConsts.VERSION).equals(firstLine)) || (!j && !b));
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
	
	private static DDDCustomDistributions loadFromJSON()
	{
		DDDCustomDistributions dists = loadDamageTypes();
		loadCreatureTypes();
		return dists;
	}
	
	private static void loadCreatureTypes()
	{
		//CREATURE TYPES FROM JSON
		if(ModConfig.resist.useCreatureTypes)
		{
			DistinctDamageDescriptions.info("Creature Types Enabled!");
			JsonParser parser = new JsonParser();
			for(File f : creatureJsonFiles)
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
						CreatureTypeData creatureType = new CreatureTypeData(type, potionImmunities, critImmunity);
						DDDRegistries.creatureTypes.register(creatureType);
						//Update map with new info
						for(JsonElement j : getJsonArray(obj, "mobs", f))
						{
							if(j.isJsonPrimitive() && j.getAsJsonPrimitive().isString())
							{
								DDDRegistries.creatureTypes.addTypeToEntity(j.getAsString(), creatureType);
							}
							else
							{
								throw new SyntaxException("Invalid Entity ID for main type in JSON "+f.getName());
							}
						}
						DistinctDamageDescriptions.debug("registered creature type: "+type);
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
			DistinctDamageDescriptions.info("Loaded Creature Types!");
		}
	}
	private static DDDCustomDistributions loadDamageTypes()
	{
		DDDCustomDistributions dists = new DDDCustomDistributions();
		//CUSTOM DAMAGE TYPES FROM JSON
		if(ModConfig.dmg.useCustomDamageTypes)
		{
			DistinctDamageDescriptions.info("Custom Damage Types Enabled!");
			JsonParser parser = new JsonParser();
			for(File f : damageTypeJsonFiles)
			{
				if(FilenameUtils.getExtension(f.getName()).equalsIgnoreCase(".json"))
				{
					continue;
				}
				else
				{
					JsonReader reader;
					try
					{
						reader = new JsonReader(new FileReader(f));
						reader.setLenient(true);
						JsonElement elem = parser.parse(reader);
						JsonObject obj = elem.getAsJsonObject();
						String name = getJsonString(obj, "name", f);
						String displayName;
						try
						{
							displayName = getJsonString(obj, "displayName", f);
						}
						catch(Exception e)
						{
							displayName = name;
						}
						int colour = Integer.parseInt(getJsonString(obj, "displayColour", f), 16);
						JsonArray arr = getJsonArray(obj, "damageTypes", f);
						JsonObject msgs = obj.get("deathMessages").getAsJsonObject();
						String entityMsg = getJsonString(msgs, "deathHasAttacker", f);
						String otherMsg = getJsonString(msgs, "deathHasNoAttacker", f);
						DamageTypeData[] datas = new DamageTypeData[arr.size()];
						int i = 0;
						for(JsonElement j : arr)
						{
							try
							{
								JsonObject dmgObj = j.getAsJsonObject();
								String damageName = getJsonString(dmgObj, "dmgSource", f);
								boolean includeAll = getJsonBoolean(dmgObj, "includeAll", f);
								Set<String> indirectSources = parsePrimitiveJsonArrayAsSet(getJsonArray(dmgObj, "indirectSources", f));
								Set<String> directSources = parsePrimitiveJsonArrayAsSet(getJsonArray(dmgObj, "directSources", f));
								datas[i++] = new DamageTypeData(damageName, directSources, indirectSources, includeAll);
							}
							catch(IllegalStateException e)
							{
								DistinctDamageDescriptions.err("Invalid Json for damage type in file "+f.getName());
								throw e;
							}
						}
						DDDDamageType type = DDDRegistries.damageTypes.get(name);
						if(type == null)
						{
							type = new DDDCustomDamageType(name, displayName, false, entityMsg, otherMsg, colour);
							DDDRegistries.damageTypes.register(type);
						}
						else
						{
							DistinctDamageDescriptions.info(String.format("ddd_%s is already registered with display name %s; will use this instead...", name, type.getDisplayName()));
						}
						dists.registerDamageTypeData(type, datas);
					}
					catch(FileNotFoundException e)
					{
						DistinctDamageDescriptions.err("Could not find JSON!");
					}	
				}
			}
			DistinctDamageDescriptions.info("Loaded Custom Damage Types!");
		}
		return dists;
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
	
	private static int getJsonInt(JsonObject obj, String memberName, File f)
	{
		return getJsonElement(obj, memberName, f).getAsInt();
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
	
	private static Set<String> parsePrimitiveJsonArrayAsSet(JsonArray arr)
	{
		Set<String> set = new HashSet<String>();
		for(JsonElement e : arr)
		{
			if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isString())
			{
				set.add(e.getAsString());
			}
			else
			{
				throw new SyntaxException("Invalid String JSON!");
			}
		}
		return set;
	}
}
