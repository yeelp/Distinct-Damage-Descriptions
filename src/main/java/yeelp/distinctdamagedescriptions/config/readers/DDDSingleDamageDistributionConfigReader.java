package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Map;
import java.util.function.Supplier;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigWrappedException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public abstract class DDDSingleDamageDistributionConfigReader extends DDDSingleStringConfigReader {

	private DDDConfigReaderException exception;
	
	public DDDSingleDamageDistributionConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) throws IllegalArgumentException {
		super(name, configSup, fallbackSup);
	}

	@Override
	protected boolean validEntry(String entry) {
		return entry.matches(ConfigReaderUtilities.DIST_REGEX);
	}

	@Override
	protected void parseEntry(String entry) {
		try {
			Map<DDDDamageType, Float> map = ConfigReaderUtilities.parseMap(this, entry, ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> 0.0f);
			if(Math.abs(YMath.sum(map.values()) - 1) <= DamageDistribution.SUM_OF_WEIGHTS_TOLERANCE) {
				this.setDistribution(this.createDistribution(map));
			}
			else {
				this.exception = new ConfigWrappedException(new InvariantViolationException(String.format("%s: %s does not have weights that add to 1!", this.getName(), entry)));
			}
		}
		catch(ConfigParsingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected DDDConfigReaderException getException() {
		return this.exception;
	}
	
	@SuppressWarnings("static-method")
	protected IDamageDistribution createDistribution(Map<DDDDamageType, Float> map) {
		return new DamageDistribution(map);
	}
	
	protected abstract void setDistribution(IDamageDistribution dist);

}
