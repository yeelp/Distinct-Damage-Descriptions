package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DDDMobResistancesConfigReader extends DDDBasicConfigReader<MobResistanceCategories> {

	private static final String IMMUNITY_REGEX = ConfigReaderUtilities.buildListRegex(ConfigReaderUtilities.DAMAGE_TYPE_SUBREGEX, true);
	private static final String RESIST_REGEX = ConfigReaderUtilities.buildListRegex(ConfigReaderUtilities.ALLOW_NEGATIVE_ENTRY_TUPLE_SUBREGEX, true);

	public DDDMobResistancesConfigReader(String[] configList) throws NoSuchMethodException, SecurityException {
		super("Mob Resistances", configList, DDDConfigurations.mobResists, MobResistanceCategories.class.getConstructor(NonNullMap.class, Collection.class, float.class, float.class), 0.0f);
	}

	@Override
	protected MobResistanceCategories parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(key.equals("player") && additionalInfo.length == 2) {
			String[] newInfo = new String[3];
			newInfo[0] = additionalInfo[0];
			newInfo[1] = additionalInfo[1].matches("0+(\\.0*)?") ? "0" : "1";
			newInfo[2] = additionalInfo[1];
			return this.parseMapping(entry, key, map, newInfo);
		}
		if(map.matches(RESIST_REGEX) && additionalInfo.length == 3) {
			if(!additionalInfo[0].matches(IMMUNITY_REGEX)) {
				throw new ConfigParsingException(this.getName(), entry);
			}
			if(!additionalInfo[1].matches(ConfigReaderUtilities.DECIMAL_REGEX) || !additionalInfo[2].matches(ConfigReaderUtilities.DECIMAL_REGEX)) {
				throw new ConfigInvalidException(this.getName(), entry);
			}
			return this.constructInstance(ConfigReaderUtilities.parseMap(this, map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> 0.0f), this.parseImmunities(entry, additionalInfo[0]), Float.parseFloat(additionalInfo[1]), Float.parseFloat(additionalInfo[2]));
		}
		throw new ConfigInvalidException(this.getName(), entry);
	}

	private Collection<DDDDamageType> parseImmunities(final String entry, String immunities) throws ConfigParsingException {
		Set<DDDDamageType> result = new HashSet<DDDDamageType>();
		if(immunities.equals("[]")) {
			return result;
		}
		for(String s : immunities.substring(1, immunities.length() - 1).split(",")) {
			result.add(ConfigReaderUtilities.validateNonNull(ConfigReaderUtilities.parseDamageType(s.trim()), () -> new ConfigParsingException(this.getName(), entry)));
		}
		return result;
	}
}
