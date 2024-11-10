package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigMalformedException extends DDDConfigReaderException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 6010590330135949898L;

	public ConfigMalformedException(String name, String entry) {
		this(name, entry, LogLevel.ERROR);
	}

	public ConfigMalformedException(String name, String entry, LogLevel level) {
		super(String.format("%s: %s is malformed! It might have a trailing semicolon. Please remove!", name, entry), level);
	}
}
