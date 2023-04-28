package yeelp.distinctdamagedescriptions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDCustomDistributions;
import yeelp.distinctdamagedescriptions.util.lib.FileHelper;

/**
 * Handles all of DDD's JSON IO
 * 
 * @author Yeelp
 *
 */
public final class DDDJsonIO {
	private static File[] creatureJsonFiles, damageTypeJsonFiles, filterJsonFiles;
	private static File creatureDirectory, damageTypeDirectory, filterDirectory;

	public static DDDCustomDistributions init() {
		File mainDirectory = DistinctDamageDescriptions.getModConfigDirectory();
		creatureDirectory = new File(mainDirectory, "creatureTypes");
		damageTypeDirectory = new File(mainDirectory, "damageTypes");
		filterDirectory = new File(mainDirectory, "filters");
		checkJSON();
		return loadFromJSON();

	}

	private static void checkJSON() {
		if((creatureDirectory.exists() && damageTypeDirectory.exists() && filterDirectory.exists()) || (damageTypeDirectory.mkdirs() && creatureDirectory.mkdirs() && filterDirectory.mkdirs())) {
			creatureJsonFiles = creatureDirectory.listFiles();
			damageTypeJsonFiles = damageTypeDirectory.listFiles();
			filterJsonFiles = filterDirectory.listFiles();
			if(writeExampleJSON("example_creature_type.json", creatureDirectory)) {
				creatureJsonFiles = creatureDirectory.listFiles();
			}
			if(writeExampleJSON("example_damage_type.json", damageTypeDirectory)) {
				damageTypeJsonFiles = damageTypeDirectory.listFiles();
			}
			if(writeExampleJSON("example_filter.json", filterDirectory)) {
				filterJsonFiles = filterDirectory.listFiles();				
			}
			DistinctDamageDescriptions.debug("Checked JSON");
		}
	}

	private static boolean writeExampleJSON(String filename, File parentDirectory) {
		if(ModConfig.core.generateJSON) {
			String relativePath = "example/" + filename;
			File dest = new File(parentDirectory, filename);
			try {
				boolean shouldOverwrite = shouldOverwriteExampleJSON(dest);
				boolean result = false;
				if(DistinctDamageDescriptions.srcFile != null && DistinctDamageDescriptions.srcFile.isDirectory()) {
					File source = new File(DistinctDamageDescriptions.srcFile, relativePath);
					return FileHelper.copyFile(source, dest, shouldOverwrite);
				}
				try(InputStream stream = DDDJsonIO.class.getClassLoader().getResourceAsStream(relativePath)) {
					result = FileHelper.copyFile(stream, dest, shouldOverwrite);
				}
				return result;
			}
			catch(IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private static boolean shouldOverwriteExampleJSON(File json) {
		boolean b = json.getParentFile().listFiles().length >= 1;
		boolean j = json.exists();
		try(FileInputStream inStream = new FileInputStream(json); BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF8"))) {
			String firstLine = reader.readLine();
			return ((j && !("// Mod Version: " + ModConsts.VERSION).equals(firstLine)) || (!j && !b));
		}
		catch(FileNotFoundException e) {
			return !b;
		}
		catch(IOException e) {
			DistinctDamageDescriptions.warn("Encountered a problem finding/parsing example creature type JSON. Will try to continue anyway...");
			return !b;
		}
	}

	private static DDDCustomDistributions loadFromJSON() {
		DDDCustomDistributions dists = loadDamageTypes();
		loadCreatureTypes();
		return dists;
	}

	public static void loadFilters() {
		
	}

	private static void loadCreatureTypes() {
		// CREATURE TYPES FROM JSON
		if(ModConfig.core.useCreatureTypes) {
			DistinctDamageDescriptions.info("Creature Types Enabled!");
			extractJsonFiles(creatureJsonFiles).forEach((f) -> {
				Tuple<CreatureTypeData, Iterable<String>> result = parseJsonFile(f, DDDCreatureTypeJsonParser::new);
				DDDRegistries.creatureTypes.register(result.getFirst());
				result.getSecond().forEach((s) -> DDDRegistries.creatureTypes.addTypeToEntity(s, result.getFirst()));
			});
			DistinctDamageDescriptions.info("Loaded Creature Types!");
		}
	}

	private static DDDCustomDistributions loadDamageTypes() {
		DDDCustomDistributions dists = new DDDCustomDistributions();
		// CUSTOM DAMAGE TYPES FROM JSON
		if(ModConfig.core.useCustomDamageTypes) {
			DistinctDamageDescriptions.info("Custom Damage Types Enabled!");
			extractJsonFiles(damageTypeJsonFiles).forEach((f) -> {
				Tuple<DDDDamageType, DamageTypeData[]> result = parseJsonFile(f, DDDDamageTypeJsonParser::new);
				// unlike creature types, damage types (not the data, which is registered below)
				// are registered in the parser's method. It's just simpler that way.
				dists.registerDamageTypeData(result.getFirst(), result.getSecond());
			});
			DistinctDamageDescriptions.info("Loaded Custom Damage Types!");
		}
		return dists;
	}

	private static <T> T parseJsonFile(File f, Function<JsonObject, ? extends AbstractJsonParser<T>> parserConstructor) {
		JsonParser parser = new JsonParser();
		try(JsonReader reader = new JsonReader(new FileReader(f))) {
			reader.setLenient(true);
			return parserConstructor.apply(parser.parse(reader).getAsJsonObject()).parseJson();
		}
		catch(IOException e) {
			DistinctDamageDescriptions.fatal("An issue occurred when reading from file: " + f.getName());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static Stream<File> extractJsonFiles(File[] files) {
		return Arrays.stream(files).filter((f) -> FilenameUtils.getExtension(f.getName()).equals(".json"));
	}
}
