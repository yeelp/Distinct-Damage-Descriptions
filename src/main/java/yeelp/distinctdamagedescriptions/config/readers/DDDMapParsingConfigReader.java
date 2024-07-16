package yeelp.distinctdamagedescriptions.config.readers;

import java.lang.reflect.Constructor;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigMalformedException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

public abstract class DDDMapParsingConfigReader<T> extends DDDMultiEntryConfigReader<T> {

	protected <U extends T> DDDMapParsingConfigReader(String name, String[] configList, IDDDConfiguration<T> config, Constructor<U> constructor) {
		super(name, configList, config, constructor);
	}

	@Override
	protected final Tuple<String, T> readEntry(String s) throws DDDConfigReaderException {
		if(s.endsWith(";")) {
			throw new ConfigMalformedException(s);
		}
		String[] args = s.split(";");
		if(args.length < 2) {
			throw new ConfigMalformedException(s);
		}
		String[] additionalInfo = new String[args.length - 2];
		System.arraycopy(args, 2, additionalInfo, 0, additionalInfo.length);
		return new Tuple<String, T>(args[0], this.parseMapping(s, args[0], args[1], additionalInfo));
	}

	protected abstract T parseMapping(final String entry, final String key, String map, String[] additionalInfo) throws DDDConfigReaderException;
}
