package yeelp.distinctdamagedescriptions.config.readers;

import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public abstract class DDDSingleStringConfigReader implements DDDConfigReader {

	private final Supplier<String> config, fallback;
	private final String name;

	public DDDSingleStringConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) {
		this.config = configSup;
		this.fallback = fallbackSup;
		this.name = name;
		if(!this.validEntry(this.fallback.get())) {
			throw new IllegalArgumentException("Fallback config entry can't be an invalid entry!");
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean shouldTime() {
		return false;
	}

	@Override
	public void read() {
		String entry = this.config.get();
		if(this.validEntry(entry)) {
			this.parseEntry(entry);
		}
		else {
			this.parseEntry(this.fallback.get());
		}
	}

	protected abstract boolean validEntry(String entry);

	protected abstract void parseEntry(String entry);
	
	/**
	 * The exception that might have been created reading this entry from the config.
	 * <p>
	 * If this reader does not self report errors ({@link #doesSelfReportErrors()}), then this should always return null.
	 * 
	 * @return The exception that was created reading the config entry, or null if the config was read successfully.
	 */
	@Nullable
	protected abstract DDDConfigReaderException getException();
	
	@Override
	public boolean doesSelfReportErrors() {
		return true;
	}
	
	@Nonnull
	@Override
	public final Collection<DDDConfigReaderException> getSelfReportedErrors() {
		if(this.getException() != null) {
			return Collections.singleton(this.getException());
		}
		return Collections.emptyList();
	}

}
