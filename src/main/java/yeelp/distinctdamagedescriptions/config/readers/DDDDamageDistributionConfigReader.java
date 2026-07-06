package yeelp.distinctdamagedescriptions.config.readers;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException.LogLevel;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

import javax.annotation.Nonnull;
import java.util.Map;

public class DDDDamageDistributionConfigReader extends DDDBasicConfigReader<IDamageDistribution> {

	public DDDDamageDistributionConfigReader(String name, String[] configList, IDDDConfiguration<IDamageDistribution> config) throws NoSuchMethodException {
		super(name, configList, config, DamageDistribution.class.getConstructor(Map.class), 0.0f);
	}

	@Nonnull
	@Override
	protected IDamageDistribution parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(map.matches(ConfigReaderUtilities.DIST_REGEX)) {
			Map<DDDDamageType, Float> distMap = ConfigReaderUtilities.parseMap(this, map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> this.defaultVal);
			if(Math.abs(distMap.values().stream().mapToDouble(Double::valueOf).filter((d) -> d > 0).sum() - 1) <= 0.01) {
				return this.constructInstance(distMap);
			}
			throw new ConfigInvalidException(new InvariantViolationException(String.format("%s: The weights for %s: %s do not add up to 1!", this.getName(), entry, distMap)));
		}
		throw new ConfigInvalidException(this.getName(), entry, LogLevel.ERROR);
	}
}
