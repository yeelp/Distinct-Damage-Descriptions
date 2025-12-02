package yeelp.distinctdamagedescriptions.config.readers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigWrappedException;
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
	
	protected DDDMultiEntryConfigReader(DDDMultiEntryConfigReader<T> ref) {
		this(ref.name, ref.configList, ref.config);
		this.constructor = ref.constructor;
		this.generator = ref.generator;
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

	protected final T constructInstance(Object... objects) {
		try {
			return Objects.requireNonNull(this.constructor, "Reader not initialized with Constructor!").newInstance(objects);
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			this.errors.add(new ConfigWrappedException(this.getName(), (e.getCause() != null) ? e.getCause() : e));
			throw new RuntimeException(e);
		}
	}

	protected final T generateInstance(Object... objects) {
		return this.generator.apply(objects);
	}

	protected abstract Tuple<String, T> readEntry(String s) throws DDDConfigReaderException;
}
