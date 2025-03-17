package yeelp.distinctdamagedescriptions.integration.electroblobswizardry;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDMultiEntryConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.GenericConfigReaderException;

public final class WizardryMinionConfigReader extends DDDMultiEntryConfigReader<String> {

	public WizardryMinionConfigReader() {
		super("Electroblob's Wizardry Minion Resistances", ModConfig.compat.ebwizardry.minionResistances, WizardryConfigurations.minionResistancesReference, (arr) -> (String) arr[0]);
	}
	
	@Override
	protected Tuple<String, String> readEntry(String s) throws DDDConfigReaderException {
		String[] arr = s.split(";");
		if(arr.length != 2) {
			throw new ConfigInvalidException(this.getName(), s);
		}
		if(!arr[1].contains(":")) {
			throw new GenericConfigReaderException(this.getName(), String.format("%s isn't a complete namespaced ID; missing a colon to split modid and entity id!", arr[1]));
		}
		return new Tuple<String, String>(arr[0], arr[1]);
	}

}
