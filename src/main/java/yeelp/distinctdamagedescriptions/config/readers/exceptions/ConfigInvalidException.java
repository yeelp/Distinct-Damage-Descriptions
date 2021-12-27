package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigInvalidException extends DDDConfigReaderException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 8807047084333393516L;

	public ConfigInvalidException(String entry) {
		this(entry, LogLevel.WARNING);
	}

	public ConfigInvalidException(String entry, LogLevel level) {
		super(String.format("%s isn't a valid entry! Ignoring...", entry), level);
	}

}
