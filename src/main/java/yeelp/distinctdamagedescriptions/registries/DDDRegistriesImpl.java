package yeelp.distinctdamagedescriptions.registries;

import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.BLUDGEONING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.PIERCING;
import static yeelp.distinctdamagedescriptions.ModConsts.InternalDamageTypes.SLASHING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.FileHelper;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.SyntaxException;

public enum DDDRegistriesImpl implements IDDDCreatureTypeRegistry, IDDDMobResistancesRegistry, IDDDMobDamageRegistry, IDDDItemPropertiesRegistry, IDDDProjectilePropertiesRegistry, IDDDDamageTypeRegistry
{
	INSTANCE;
	private final Map<String, MobResistanceCategories> mobResists = new NonNullMap<String, MobResistanceCategories>(new MobResistanceCategories(new NonNullMap<String, Float>(0.0f), new HashSet<String>(), 0, 0));
	private final Map<String, CreatureTypeData> creatureTypes = new NonNullMap<String, CreatureTypeData>(CreatureTypeData.UNKNOWN);
	private final Map<String, Set<String>> creatureMap = new NonNullMap<String, Set<String>>(new HashSet<String>());
	private final Map<String, Map<String, Float>> mobDamage = new NonNullMap<String, Map<String, Float>>(buildMap(0.0f, new Tuple<String, Float>(BLUDGEONING, 1.0f)));
	private final Map<String, Map<String, Float>> itemArmorDist = new NonNullMap<String, Map<String, Float>>(new NonNullMap<String, Float>(0.0f));
    private final Map<String, Map<String, Float>> itemDamageDist = new NonNullMap<String, Map<String, Float>>(((NonNullMap<String, Map<String, Float>>)mobDamage).getDefaultValue());
    private final Map<String, Map<String, Float>> projectileDist = new NonNullMap<String, Map<String, Float>>(buildMap(0.0f, new Tuple<String, Float>(PIERCING, 1.0f)));
    private final Map<String, Map<String, Float>> shieldDist = new NonNullMap<String, Map<String, Float>>(new NonNullMap<String, Float>(1.0f));
    private final Map<String, String> itemIDToProjIDMap = new HashMap<String, String>();
    private final Map<String, Tuple<Map<String, String>, Map<String, String>>> damageTypeMap = new HashMap<String, Tuple<Map<String, String>, Map<String, String>>>();
    private final Map<String, Tuple<String, String>> deathMessages = new HashMap<String, Tuple<String, String>>();
    private final Map<String, String> includeAllMap = new NonNullMap<String, String>("ddd_normal");
    private final Map<String, String> displayInfo = new HashMap<String, String>();
    private static File[] creatureJsonFiles, damageTypeJsonFiles;
	private static File creatureDirectory, damageTypeDirectory;
	DDDRegistriesImpl()
	{
		DDDRegistries.creatureTypes = this;
		DDDRegistries.mobResists = this;
		DDDRegistries.mobDamage = this;
		DDDRegistries.itemProperties = this;
		DDDRegistries.projectileProperties = this;
		DDDRegistries.damageTypes = this;
		
		try
		{
			init();
		}
		catch(Exception e)
		{
			DistinctDamageDescriptions.fatal("Encountered severe error loading registries!");
			DistinctDamageDescriptions.fatal("Exception type: "+e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
			DistinctDamageDescriptions.fatal("Trace: "+Arrays.toString(e.getStackTrace()));
			throw e;
		}
	}
	
	@Override
	public void init()
	{
		File mainDirectory = DistinctDamageDescriptions.getModConfigDirectory();
		creatureDirectory = new File(mainDirectory, "creatureTypes");
		damageTypeDirectory = new File(mainDirectory, "damageTypes");
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
			load();
		}
	}	

	@Override
	public Optional<MobResistanceCategories> getResistancesForMob(String key)
	{
		return optionalGet(mobResists, key);
	}

	@Override
	public Set<CreatureTypeData> getCreatureTypeForMob(String key)
	{
		HashSet<CreatureTypeData> set = new HashSet<CreatureTypeData>();
		for(String s : creatureMap.get(key))
		{
			set.add(creatureTypes.get(s));
		}
		if(!set.isEmpty())
		{
			return set;
		}
		else
		{
			return new HashSet<CreatureTypeData>(ImmutableList.of(CreatureTypeData.UNKNOWN));
		}
	}

	@Override
	public CreatureTypeData getCreatureTypeData(String key)
	{
		return creatureTypes.get(key);
	}
	
	@Override
	public Optional<Map<String, Float>> getMobDamage(String key)
	{
		return optionalGet(mobDamage, key);
	}
	
	@Override
	public Optional<Map<String, Float>> getDamageDistributionForItem(String key)
	{
		return optionalGet(itemDamageDist, key);
	}
	
	@Override
	public Map<String, Float> getDefaultDamageDistribution()
	{
		return ((NonNullMap<String, Map<String, Float>>) itemDamageDist).getDefaultValue();
	}
	
	@Override
	public boolean doesItemHaveCustomDamageDistribution(String key)
	{
		return itemDamageDist.containsKey(key);
	}
	
	@Override
	public Map<String, Float> getDefaultArmorDistribution()
	{
		return ((NonNullMap<String, Map<String, Float>>) itemArmorDist).getDefaultValue();
	}
	
	@Override 
	public Optional<Map<String, Float>> getArmorDistributionForItem(String key)
	{
		return optionalGet(itemArmorDist, key);
	}
	
	@Override
	public boolean doesArmorHaveCustomArmorDistribution(String key)
	{
		return itemArmorDist.containsKey(key);
	}
	
	@Override
	public Optional<Map<String, Float>> getShieldDistribution(String key)
	{
		return optionalGet(shieldDist, key);
	}
	
	@Override
	public Optional<Map<String, Float>> getProjectileDamageTypes(String key)
    {
    	return optionalGet(projectileDist, key);
    }
    
	@Override
    public Map<String, Float> getProjectileDamageTypesFromItemID(String itemID)
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
	
	@Override
	public boolean isProjectileRegistered(Entity projectile)
	{
		return projectileDist.containsKey(EntityList.getKey(projectile).toString());
	}
	
	@Override
	public void registerDamageType(String name, String displayName, TextFormatting colour, String entityMsg, String otherMsg, DamageTypeData...datas)
	{
		name = "ddd_"+name;
		displayInfo.put(name, new TextComponentString(displayName).setStyle(new Style().setColor(colour)).getFormattedText());
		deathMessages.put(name, new Tuple<String, String>(entityMsg, otherMsg));
		for(DamageTypeData d : datas)
		{
			Map<String, String> direct = new NonNullMap<String, String>("ddd_normal"), indirect = new NonNullMap<String, String>("ddd_normal");
			if(d.includeAll())
			{
				includeAllMap.put(d.getOriginalSource(), name);
			}
			for(String s : d.getDirectSources())
			{
				direct.put(s, name);
			}
			for(String s : d.getIndirectSources())
			{
				indirect.put(s, name);
			}
			
			damageTypeMap.compute(d.getOriginalSource(), (s, t) -> damageTypeMap.containsKey(s) ? updateTuple(t, direct, indirect) : new Tuple<Map<String, String>, Map<String, String>>(direct, indirect));
		}
		DistinctDamageDescriptions.debug("Registered damage type: "+name);
	}
	
	@Override
	public String getDisplayName(String name)
	{
		return displayInfo.get(name);
	}
	
	@Override
	public Set<String> getCustomDamageContext(DamageSource originalSource)
	{
		Tuple<Map<String, String>, Map<String, String>> t = damageTypeMap.get(originalSource.damageType);
		Set<String> types = new HashSet<String>();
		types.add(includeAllMap.get(originalSource.getDamageType()));
		if(t == null)
		{
			return types;
		}
		else
		{
			ResourceLocation directLoc = EntityList.getKey(originalSource.getImmediateSource()), indirectLoc = EntityList.getKey(originalSource.getTrueSource());
			String direct = directLoc != null ? directLoc.toString() : "", indirect = indirectLoc != null ? indirectLoc.toString(): "";
			DistinctDamageDescriptions.debug(direct+", "+indirect);
			DistinctDamageDescriptions.debug(t.getFirst().get(direct)+", "+t.getSecond().get(indirect));
			types.add(t.getFirst().get(direct));
			types.add(t.getSecond().get(indirect));
			return types;
		}
	}
	
	@Override
	public String getDeathMessage(@Nonnull String type, @Nonnull String defenderName, @Nullable String attackerName)
	{
		Tuple<String, String> t = deathMessages.get(type);
		if(t == null)
		{
			DistinctDamageDescriptions.debug("This makes no sense hello??");
			DistinctDamageDescriptions.debug(String.format("%s, %s %s", type, defenderName, attackerName == null ? "null" : attackerName));
		}
		if(attackerName == null)
		{
			return deathMessages.get(type).getSecond().replaceAll("#defender", defenderName);
		}
		else
		{
			return deathMessages.get(type).getFirst().replaceAll("#attacker", attackerName).replaceAll("#defender", defenderName);
		}
	}
	
	private static Tuple<Map<String, String>, Map<String, String>> updateTuple(Tuple<Map<String,String>, Map<String, String>> t, Map<String, String> direct, Map<String, String> indirect)
	{
		t.getFirst().putAll(direct);
		t.getSecond().putAll(indirect);
		return t;
	}
	
	private static String[] tryPut(float defaultVal, Map<String, Map<String, Float>> map, String s, int customInfoIndex)
    {
    	String[] contents = s.split(";");
    	try
		{
    		String customInfo = contents.length-1 < customInfoIndex ? "" : contents[customInfoIndex];
    		NonNullMap<String, Float> dist = buildMap(defaultVal, parseStringOfTuples(parseConfigString(customInfo)));
    		dist.put(SLASHING, Float.valueOf(contents[1]));
    		dist.put(PIERCING, Float.valueOf(contents[2]));
    		dist.put(BLUDGEONING, Float.valueOf(contents[3]));
    		//We need to remove all zero entries in the map, as this will mess up adaptive resistance (it will think the damage was inflicted and adapt to it).
    		dist.remove(SLASHING, 0.0f);
    		dist.remove(PIERCING, 0.0f);
    		dist.remove(BLUDGEONING, 0.0f);
			map.put(contents[0], dist);
			return contents;
		}
		catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			DistinctDamageDescriptions.warn(s+" isn't a valid entry! Ignoring...");
		}
    	return null;
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
	
	private void load()
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
						//Update map with new info
						for(JsonElement j : getJsonArray(obj, "mobs", f))
						{
							if(j.isJsonPrimitive() && j.getAsJsonPrimitive().isString())
							{
								creatureMap.get(j.getAsString()).add(type);
							}
							else
							{
								throw new SyntaxException("Invalid Entity ID for main type in JSON "+f.getName());
							}
						}
						creatureTypes.put(type, new CreatureTypeData(type, potionImmunities, critImmunity));
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
						TextFormatting colour = TextFormatting.getValueByName(getJsonString(obj, "displayColour", f));
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
						registerDamageType(name, displayName, colour, entityMsg, otherMsg, datas);
					}
					catch(FileNotFoundException e)
					{
						DistinctDamageDescriptions.err("Could not find JSON!");
					}	
				}
			}
			DistinctDamageDescriptions.info("Loaded Custom Damage Types!");
		}
		//MOB RESISTANCES FROM CONFIG
		for(String s : ModConfig.resist.mobBaseResist)
		{
			try
			{
				String[] contents = s.split(";");
				Map<String, Float> resists;
				Set<String> immunities= new HashSet<String>();
				if(contents.length == 9)
				{
					resists = buildMap(0.0f, parseStringOfTuples(parseConfigString(contents[7])));
					immunities = parseImmunitiesFromArray(contents[8]);
				}
				else
				{
					resists = new NonNullMap<String, Float>(0.0f);
				}
				resists.put(SLASHING, Float.valueOf(contents[1]));
				resists.put(PIERCING, Float.valueOf(contents[2]));
				resists.put(BLUDGEONING, Float.valueOf(contents[3]));
				resists.remove(SLASHING, 0.0f);
				resists.remove(PIERCING, 0.0f);
				resists.remove(BLUDGEONING, 0.0f);
				if(contents[4].contains("s"))
				{
					immunities.add(SLASHING);
				}
				if(contents[4].contains("p"))
				{
					immunities.add(PIERCING);
				}
				if(contents[4].contains("b"))
				{
					immunities.add(BLUDGEONING);
				}
				mobResists.put(contents[0], new MobResistanceCategories(resists, immunities, Float.valueOf(contents[5]), Float.valueOf(contents[6])));
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
			tryPut(0.0f, mobDamage, s, 4);
		}
		DistinctDamageDescriptions.info("Mob damage loaded!");
		//ARMOR RESISTANCES FROM CONFIG
		for(String s : ModConfig.resist.armorResist)
		{
			tryPut(0.0f, itemArmorDist, s, 4);
		}
		//SHIELD EFFECTIVENESS FROM CONFIG
		for(String s : ModConfig.resist.shieldResist)
		{
			tryPut(0.0f, shieldDist, s, 4);
		}
		DistinctDamageDescriptions.info("Armor resistances loaded!");
		//WEAPON DAMAGE FROM CONFIG
		for(String s : ModConfig.dmg.itemBaseDamage)
		{
			tryPut(0.0f, itemDamageDist, s, 4);
		}
		DistinctDamageDescriptions.info("Weapon damage loaded!");
		//Projectile Damage Types from Config
		Pattern digits = Pattern.compile("\\A\\d++(\\.\\d++)?\\z");
		for(String s : ModConfig.dmg.projectileDamageTypes)
		{
			int index = s.lastIndexOf(";");
			if(index == -1)
			{
				DistinctDamageDescriptions.warn(s + "Isn't a valid entry! Ignoring...");
			}
			String ids = s.substring(index+1);
			if(!ids.startsWith("[") && !digits.matcher(ids).find()) //If false, this isn't a list of ids, it's a list of custom damage types
			{
				s = s.substring(0, index);
			}
			else
			{
				ids = null;
			}
			String[] entry = tryPut(0.0f, projectileDist, s, 4);
			if(entry != null && ids != null)
			{
				if(!ids.trim().isEmpty())
				{
					for(String i : ids.split(","))
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
	
	private static <T> Optional<T> optionalGet(Map<String, T> map, String key)
	{
		return !ModConfig.generateStats || map.containsKey(key) ? Optional.of(map.get(key)) : Optional.empty();
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
	
	@SafeVarargs
	private static <K, V> NonNullMap<K, V> buildMap(V defaultVal, Tuple<K, V>...mappings)
	{
		return buildMap(defaultVal, Arrays.asList(mappings));
	}
	
	
	@SuppressWarnings("unchecked")
	private static Iterable<Tuple<String, Float>> parseStringOfTuples(String[] strings)
	{
		if(strings == null)
		{
			return null;
		}
		else if(strings.length == 0)
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			List<Tuple<String, Float>> ts = new ArrayList<Tuple<String, Float>>();
			for(String s : strings)
			{
				String[] temp = s.split(", ");
				ts.add(new Tuple<String, Float>(temp[0], Float.parseFloat(temp[1])));
			}
			return ts;
		}
	}
	
	private static String[] parseConfigString(String s)
	{
		if(s.equals(""))
		{
			return null;
		}
		else if(s.equals("[]"))
		{
			return new String[0];
		}
		else
		{
			//Split the string at every instance of "),(" or "), (", trim off ends, return.
			String[] arr = s.split("\\),(?:\\s?)\\(");
			arr[0] = arr[0].substring(2); //remove "[(" prefix
			String temp = arr[arr.length-1];
			temp = temp.substring(0, temp.length()-2); //remove ")]" suffix
			arr[arr.length-1] = temp;
			return arr;
		}
	}
	
	private static Set<String> parseImmunitiesFromArray(String s)
	{
		Set<String> set = new HashSet<String>();
		if(s.equals("") || s.equals("[]"))
		{
			return set;
		}
		else
		{
			String[] arr = s.substring(1, s.length() - 1).split(",");
			for(String str : arr)
			{
				set.add(str.trim());
			}
			return set;
		}
	}
}
