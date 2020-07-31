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
import java.util.Map.Entry;
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

public interface IDDDCreatureTypeRegistry extends IDDDRegistry
{
	
	
	/**
	 * Get the CreatureTypeData(s) for a mob
	 * @param entity the entitylivingbase
	 * @return a Tuple of CreatureTypeData. If the first is CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	public default Tuple<CreatureTypeData, CreatureTypeData> getCreatureTypeForMob(EntityLivingBase entity)
	{
		return getCreatureTypeForMob(EntityList.getKey(entity).toString());
	}
	
	/**
	 * Get the CreatureTypeData(s) for a mob
	 * @param namespaced id of the mob
	 * @return a Tuple of CreatureTypeData. If the first is CreatureTypeData.UNKNOWN, then this mob has no creature type.
	 */
	public Tuple<CreatureTypeData, CreatureTypeData> getCreatureTypeForMob(String key);
	
	/**
	 * Get CreatureTypeData from it's name
	 * @param key the name
	 * @return the CreatureTypeData
	 */
	public CreatureTypeData getCreatureTypeData(String key);
}
