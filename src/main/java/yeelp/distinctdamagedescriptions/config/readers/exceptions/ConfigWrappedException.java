package yeelp.distinctdamagedescriptions.config.readers.exceptions;

public final class ConfigWrappedException extends DDDConfigReaderException {

	public ConfigWrappedException(String name, Throwable cause) {
		super((name != null ? name + ": " : "") + cause.getLocalizedMessage(), LogLevel.FATAL);
	} 
	
	public ConfigWrappedException(Throwable cause) {
		this(null, cause);
	}
}
