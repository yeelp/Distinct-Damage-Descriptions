package yeelp.distinctdamagedescriptions.config.readers;

import java.util.function.Function;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public class DDDNumericEntryConfigReader<T extends Number> extends DDDMultiEntryConfigReader<T> {

	public DDDNumericEntryConfigReader(String name, String[] configList, IDDDConfiguration<T> config, Function<String, T> factory) {
		super(name, configList, config, (objs) -> factory.apply(objs[0].toString()));
	}

	@Override
	protected Tuple<String, T> readEntry(String s) throws DDDConfigReaderException {
		String[] args = s.split(";");
		if(args.length != 2 || !args[1].matches(ConfigReaderUtilities.DECIMAL_REGEX)) {
			throw new ConfigInvalidException(this.getName(), s);
		}
		return new Tuple<String, T>(args[0], this.generateInstance(args[1]));
	}

}
