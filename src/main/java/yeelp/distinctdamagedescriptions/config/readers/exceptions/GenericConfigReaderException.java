package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class GenericConfigReaderException extends DDDConfigReaderException {

	public GenericConfigReaderException(String name, String msg) {
		this(name, msg, LogLevel.ERROR);
	}
	
	public GenericConfigReaderException(String name, String msg, LogLevel level) {
		super(String.format("%s: %s", name, msg), level);
	}

}
