package yeelp.distinctdamagedescriptions.util.development;

import java.util.function.Function;
import java.util.function.Predicate;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

final class DeveloperModeUtilities {

	private DeveloperModeUtilities() {

	}

	static String damageTypeRegistryToString(Predicate<DDDDamageType> filter, Function<DDDDamageType, String> func) {
		return "[" + YLib.joinNiceString(true, ",", DDDRegistries.damageTypes.getAll().stream().filter(filter).map(func).toArray(String[]::new)) + "]";
	}
}
