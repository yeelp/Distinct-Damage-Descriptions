package yeelp.distinctdamagedescriptions.config.readers;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;

public final class DDDProjectileConfigReader extends DDDBasicConfigReader<IDamageDistribution> {

	public DDDProjectileConfigReader() throws NoSuchMethodException, SecurityException {
		super("Projectile Distributions", ModConfig.dmg.projectileDamageTypes, DDDConfigurations.projectiles, DamageDistribution.class.getConstructor(Map.class), 0.0f);
	}

	@Override
	protected IDamageDistribution parseMapping(final String entry, final String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		Stream<String> projectiles = Stream.empty();
		if(additionalInfo.length == 1) {
			projectiles = Arrays.stream(additionalInfo[0].split(",")).map(String::trim);			
		}
		else if(additionalInfo.length > 1) {
			throw new ConfigInvalidException(entry);
		}
		IDamageDistribution dist = super.parseMapping(entry, key, map, additionalInfo);
		projectiles.forEach((s) -> DDDConfigurations.projectiles.registerItemProjectilePair(s, key));
		return dist;
	}
}
