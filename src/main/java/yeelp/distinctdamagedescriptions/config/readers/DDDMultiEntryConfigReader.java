package yeelp.distinctdamagedescriptions.config.readers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

/**
 * A skeletal reader that reads lists of config entries.
 * 
 * @author Yeelp
 *
 * @param <T> The type of config entries this reader parses
 */
public abstract class DDDMultiEntryConfigReader<T> implements DDDConfigReader {
	private final String[] configList;
	private final String name;
	private final IDDDConfiguration<T> config;
	private final Collection<DDDConfigReaderException> errors;
	// We have a choice, do we want a Constructor, which will need reflection, but
	// the actual code is being reused, or do we want to specify how to build our
	// entry from scratch?
	private Constructor<? extends T> constructor;
	private Function<Object[], ? extends T> generator;

	@Deprecated
	private static final Collection<DDDConfigReaderException> ERRORS = Collections.synchronizedCollection(new LinkedList<DDDConfigReaderException>());

	private DDDMultiEntryConfigReader(String name, String[] configList, IDDDConfiguration<T> config) {
		this.configList = configList;
		this.config = config;
		this.name = name;
		this.errors = Lists.newArrayList();
	}

	protected <U extends T> DDDMultiEntryConfigReader(String name, String[] configList, IDDDConfiguration<T> config, Constructor<U> constructor) {
		this(name, configList, config);
		this.constructor = constructor;

	}

	protected <U extends T> DDDMultiEntryConfigReader(String name, String[] configList, IDDDConfiguration<T> config, Function<Object[], U> generator) {
		this(name, configList, config);
		this.generator = generator;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean shouldTime() {
		return true;
	}

	/**
	 * Read from the config and populate the runtime config
	 */
	@Override
	public final void read() {
		for(String string : this.configList) {
			if(ConfigReaderUtilities.isCommentEntry(string)) {
				DistinctDamageDescriptions.debug("Encountered Config Comment...");
				continue;
			}
			Tuple<String, ? extends T> t;
			try {
				t = this.readEntry(string);
			}
			catch(DDDConfigReaderException e) {
				this.errors.add(e);
				continue;
			}
			this.config.put(t.getFirst(), t.getSecond());
		}
	}
	
	@Override
	public boolean doesSelfReportErrors() {
		return true;
	}
	
	@Override
	public Collection<DDDConfigReaderException> getSelfReportedErrors() {
		return this.errors;
	}

	/**
	 * Display all errors accumulated from all config readers thus far.
	 */
	@Deprecated
	public static final void displayErrors() {
		synchronized(ERRORS) {
			ERRORS.forEach(DDDConfigReaderException::log);
		}
	}
	
	@Deprecated
	public static boolean hasErrors() {
		synchronized(ERRORS) {
			return ERRORS.size() > 0;
		}
	}

	@Deprecated
	public static final Collection<String> getErrorMessages() {
		synchronized(ERRORS) {
			return ERRORS.stream().map(DDDConfigReaderException::getLocalizedMessage).collect(Collectors.toList());			
		}
	}

	protected final T constructInstance(Object... objects) {
		try {
			return Objects.requireNonNull(this.constructor, "Reader not initialized with Constructor!").newInstance(objects);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// if this happens, something likely went terribly wrong on my part, just stop
			// everything.
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected final T generateInstance(Object... objects) {
		return this.generator.apply(objects);
	}

	protected abstract Tuple<String, T> readEntry(String s) throws DDDConfigReaderException;
}
