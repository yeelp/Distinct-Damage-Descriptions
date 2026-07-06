package yeelp.distinctdamagedescriptions.config.readers;

import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

import javax.annotation.Nonnull;
import java.util.Map;

public final class DDDArmorDistributionConfigReader extends DDDBasicConfigReader<IArmorDistribution> {

	public DDDArmorDistributionConfigReader(String name, String[] configList, IDDDConfiguration<IArmorDistribution> config) throws NoSuchMethodException {
		super(name, configList, config, ArmorDistribution.class.getConstructor(Map.class), 0.0f);
	}

	@Nonnull
	@Override
	protected IArmorDistribution parseMapping(String entry, String key, String map, String[] additionalInfo) throws ConfigInvalidException, ConfigParsingException {
		IArmorDistribution dist;
		if(map.matches(ConfigReaderUtilities.EMPTY_DIST)) {
			dist = new ArmorDistribution();
		}
		else {
			DebugLib.outputFormattedDebug("Key: %s, Map: %s", key, map);
			dist = super.parseMapping(entry, key, map, additionalInfo);
			DebugLib.outputFormattedDebug("After parsing %s: %s", key, dist.toString());
		}
		return ModConfig.resist.armorParseRule.apply(dist);
	}
}