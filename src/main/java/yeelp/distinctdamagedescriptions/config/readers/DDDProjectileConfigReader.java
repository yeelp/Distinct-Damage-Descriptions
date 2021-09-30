package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Arrays;
import java.util.Map;

import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;

public final class DDDProjectileConfigReader extends DDDBasicConfigReader<IDamageDistribution> {

	public DDDProjectileConfigReader() throws NoSuchMethodException, SecurityException {
		super(ModConfig.dmg.projectileDamageTypes, DDDConfigurations.projectiles, DamageDistribution.class.getConstructor(Map.class), 0.0f);
	}

	@Override
	protected IDamageDistribution parseMapping(final String entry, final String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		if(additionalInfo.length == 1) {
			Arrays.stream(additionalInfo[0].split(",")).map(String::trim).forEach((s) -> DDDConfigurations.projectiles.registerItemProjectilePair(s, key));			
		}
		else if(additionalInfo.length > 1) {
			throw new ConfigInvalidException(entry);
		}
		return super.parseMapping(entry, key, map, additionalInfo);
	}
}
