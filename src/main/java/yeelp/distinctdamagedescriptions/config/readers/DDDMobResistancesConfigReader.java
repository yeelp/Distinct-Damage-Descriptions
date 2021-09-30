package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public final class DDDMobResistancesConfigReader extends DDDBasicConfigReader<MobResistanceCategories> {

	private static final String IMMUNITY_REGEX = ConfigReaderUtilities.buildListRegex(ConfigReaderUtilities.DAMAGE_TYPE_SUBREGEX, true);
	private static final String RESIST_REGEX = ConfigReaderUtilities.buildListRegex(ConfigReaderUtilities.ALLOW_NEGATIVE_ENTRY_TUPLE_SUBREGEX, true);
	public DDDMobResistancesConfigReader(String[] configList) throws NoSuchMethodException, SecurityException {
		super(configList, DDDConfigurations.mobResists, MobResistanceCategories.class.getConstructor(Map.class, Collection.class, float.class, float.class), 0.0f);
	}

	@Override
	protected MobResistanceCategories parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(map.matches(RESIST_REGEX) && additionalInfo.length == 3) {
			if(!additionalInfo[0].matches(IMMUNITY_REGEX)) {
				throw new ConfigParsingException(entry);
			}
			else if (!additionalInfo[1].matches(ConfigReaderUtilities.DECIMAL_REGEX) || !additionalInfo[2].matches(ConfigReaderUtilities.DECIMAL_REGEX)) {
				throw new ConfigInvalidException(entry);
			}
			return this.constructInstance(ConfigReaderUtilities.parseMap(map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, 0.0f), parseImmunities(entry, additionalInfo[0]), Float.parseFloat(additionalInfo[1]), Float.parseFloat(additionalInfo[2]));
		}
		throw new ConfigInvalidException(entry);
	}
	
	private static Collection<DDDDamageType> parseImmunities(final String entry, String immunities) throws ConfigParsingException {
		Set<DDDDamageType> result = new HashSet<DDDDamageType>();
		if(immunities.equals("[]")) {
			return result;
		}
		for(String s : immunities.substring(1, immunities.length() - 1).split(",")) {
			result.add(ConfigReaderUtilities.validateNonNull(ConfigReaderUtilities.parseDamageType(s.trim()), () -> new ConfigParsingException(entry)));
		}
		return result;
	}
}
