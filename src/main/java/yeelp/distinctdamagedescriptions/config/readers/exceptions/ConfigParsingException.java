package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigParsingException extends DDDConfigReaderException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 7243426801035275543L;

	public ConfigParsingException(String entry) {
		this(entry, LogLevel.FATAL);
	}

	public ConfigParsingException(String entry, LogLevel level) {
		super(String.format("%s refers to an invalid damage type that doesn't exist! Check spelling!", entry), level);
	}

}
