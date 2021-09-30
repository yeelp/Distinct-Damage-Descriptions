package yeelp.distinctdamagedescriptions.config.readers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

/**
 * A skeletal reader that reads lists of config entries.
 * @author Yeelp
 *
 * @param <T> The type of config entries this reader parses
 */
public abstract class DDDMultiEntryConfigReader<T> implements DDDConfigReader {
	private final String[] configList;
	private final IDDDConfiguration<T> config;
	private final Constructor<? extends T> constructor;
	
	private static final Collection<DDDConfigReaderException> ERRORS = new LinkedList<DDDConfigReaderException>();
	
	protected <U extends T> DDDMultiEntryConfigReader(String[] configList, IDDDConfiguration<T> config, Constructor<U> constructor) {
		this.configList = configList;
		this.config = config;
		this.constructor = constructor;
	}
	
	/**
	 * Read from the config and populate the runtime config
	 */
	@Override
	public final void read() {
		for(String string : this.configList) {
			Tuple<String, ? extends T> t;
			try {
				t = this.readEntry(string);
			}
			catch(DDDConfigReaderException e) {
				ERRORS.add(e);
				continue;
			}
			this.config.put(t.getFirst(), t.getSecond());
		}
	}
	
	/**
	 * Display all errors accumulated from all config readers thus far.
	 */
	public static final void displayErrors() {
		if(ERRORS.size() > 0) {
			DistinctDamageDescriptions.fatal("There were some problems reading from the config! Check the following:");
			ERRORS.forEach(DDDConfigReaderException::log);
		}
	}
	
	protected final T constructInstance(Object...objects) {
		try {
			return this.constructor.newInstance(objects);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//if this happens, something likely went terribly wrong on my part, just stop everything.
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected abstract Tuple<String, T> readEntry(String s) throws DDDConfigReaderException;
}
