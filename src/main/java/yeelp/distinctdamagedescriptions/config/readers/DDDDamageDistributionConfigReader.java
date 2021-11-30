package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Map;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException.LogLevel;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public final class DDDDamageDistributionConfigReader extends DDDBasicConfigReader<IDamageDistribution> {

	public DDDDamageDistributionConfigReader(String name, String[] configList, IDDDConfiguration<IDamageDistribution> config) throws NoSuchMethodException, SecurityException {
		super(name, configList, config, DamageDistribution.class.getConstructor(Map.class), 0.0f);
	}

	@Override
	protected IDamageDistribution parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(map.matches(ConfigReaderUtilities.DIST_REGEX)) {
			Map<DDDDamageType, Float> distMap = ConfigReaderUtilities.parseMap(map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> this.defaultVal);
			if(Math.abs(distMap.values().stream().mapToDouble(Double::valueOf).filter((d) -> d > 0).sum() - 1) <= 0.01) {
				return this.constructInstance(distMap);
			}
		}
		throw new ConfigInvalidException(entry, LogLevel.ERROR);
	}
}
