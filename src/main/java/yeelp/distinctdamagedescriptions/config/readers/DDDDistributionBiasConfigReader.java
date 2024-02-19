package yeelp.distinctdamagedescriptions.config.readers;

import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDDistributionBiasConfigReader extends DDDBasicConfigReader<DistributionBias> {

	public DDDDistributionBiasConfigReader(String name, String[] configList, IDDDConfiguration<DistributionBias> config) throws NoSuchMethodException, SecurityException {
		super(name, configList, config, DistributionBias.class.getConstructor(NonNullMap.class, float.class), 0.0f);
	}

	@Override
	protected DistributionBias parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(additionalInfo.length > 1 || !additionalInfo[0].matches(ConfigReaderUtilities.DECIMAL_REGEX)) {
			throw new ConfigInvalidException(entry);
		}
		return this.constructInstance(ConfigReaderUtilities.parseMap(map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> this.defaultVal), Float.parseFloat(additionalInfo[0]));
	}
}
