package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.readers;

import java.util.Optional;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.GenericConfigReaderException;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;

public final class WizardryMinionConfigReader extends AbstractWizardryConfigReader {

	public WizardryMinionConfigReader() {
		super("Electroblob's Wizardry Minion Capabilities", ModConfig.compat.ebwizardry.minionCapabilities, WizardryConfigurations.minionCapabilityReference);
	}
	
	@Override
	protected Optional<? extends DDDConfigReaderException> validateEntry(String[] arr) {
		if(!arr[1].contains(":")) {
			return Optional.of(new GenericConfigReaderException(this.getName(), String.format("%s isn't a complete namespaced ID; missing a colon to split modid and entity id!", arr[1])));
		}
		return Optional.empty();
	}

}
