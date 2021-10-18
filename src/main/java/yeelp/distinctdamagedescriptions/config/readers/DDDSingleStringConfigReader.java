package yeelp.distinctdamagedescriptions.config.readers;

import java.util.function.Supplier;

public abstract class DDDSingleStringConfigReader implements DDDConfigReader {

	private final Supplier<String> config, fallback;
	private final String name;

	public DDDSingleStringConfigReader(String name, Supplier<String> configSup, Supplier<String> fallbackSup) throws IllegalArgumentException {
		this.config = configSup;
		this.fallback = fallbackSup;
		this.name = name;
		if(!this.validEntry(this.fallback.get())) {
			throw new IllegalArgumentException("Fallback config entry can't be an invalid entry!");
		}
	}

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

}
