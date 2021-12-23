package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigMalformedException extends DDDConfigReaderException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 6010590330135949898L;

	public ConfigMalformedException(String entry) {
		this(entry, LogLevel.ERROR);
	}

	public ConfigMalformedException(String entry, LogLevel level) {
		super(String.format("%s is malformed! It might have a trailing semicolon. Please remove!", entry), level);
	}
}
