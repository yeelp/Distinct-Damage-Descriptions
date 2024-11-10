package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigInvalidException extends DDDConfigReaderException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 8807047084333393516L;

	public ConfigInvalidException(String name, String entry) {
		this(name, entry, LogLevel.WARNING);
	}

	public ConfigInvalidException(String name, String entry, LogLevel level) {
		super(String.format("%s: %s isn't a valid entry! Ignoring...", name, entry), level);
	}
	
	public ConfigInvalidException(Exception cause) {
		super(cause, LogLevel.FATAL);
	}

}
