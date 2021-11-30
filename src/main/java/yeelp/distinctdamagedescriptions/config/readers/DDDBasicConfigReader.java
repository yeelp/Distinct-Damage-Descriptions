package yeelp.distinctdamagedescriptions.config.readers;

import java.lang.reflect.Constructor;

import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public class DDDBasicConfigReader<T> extends DDDMapParsingConfigReader<T> {
	
	protected final float defaultVal;

	public <U extends T> DDDBasicConfigReader(String name, String[] configList, IDDDConfiguration<T> config, Constructor<U> constructor, float defaultVal) {
		super(name, configList, config, constructor);
		this.defaultVal = defaultVal;
	}

	@Override
	protected T parseMapping(final String entry, final String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(map.matches(ConfigReaderUtilities.DIST_REGEX)) {
			return this.constructInstance(ConfigReaderUtilities.parseMap(map, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> this.defaultVal));
		}
		throw new ConfigInvalidException(entry);
	}
}
