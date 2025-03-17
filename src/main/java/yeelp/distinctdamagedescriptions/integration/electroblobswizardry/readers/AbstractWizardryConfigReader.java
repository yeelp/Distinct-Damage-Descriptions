package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.readers;

import java.util.Optional;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDModIDPrependingConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.DDDMultiEntryConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

public abstract class AbstractWizardryConfigReader extends DDDMultiEntryConfigReader<String> {

	protected AbstractWizardryConfigReader(String name, String[] configList, IDDDConfiguration<String> config) {
		super(name, configList, config, (args) -> (String) args[0]);
	}

	@Override
	protected final Tuple<String, String> readEntry(String s) throws DDDConfigReaderException {
		String[] arr = s.split(";");
		if(arr.length != 2) {
			throw new ConfigInvalidException(this.getName(), s);
		}
		Optional<? extends DDDConfigReaderException> invalid = this.validateEntry(arr);
		if(invalid.isPresent()) {
			throw invalid.get();
		}
		return new Tuple<String, String>(arr[0], arr[1]);
	}

	public DDDConfigReader wrapInModIDConfigReader() {
		return new DDDModIDPrependingConfigReader<String>(IntegrationIds.WIZARDRY_ID, this);
	}

	protected abstract Optional<? extends DDDConfigReaderException> validateEntry(String[] arr);

	public static final class BasicWizardryConfigReader extends AbstractWizardryConfigReader {

		public BasicWizardryConfigReader(String name, String[] configList, IDDDConfiguration<String> config) {
			super(name, configList, config);
		}

		@Override
		protected Optional<? extends DDDConfigReaderException> validateEntry(String[] arr) {
			return Optional.empty();
		}

	}

}
