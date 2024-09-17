package yeelp.distinctdamagedescriptions.config.readers.exceptions;

import java.util.function.Consumer;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;

public abstract class DDDConfigReaderException extends Exception {

	public enum LogLevel {
		WARNING(DistinctDamageDescriptions::warn),
		ERROR(DistinctDamageDescriptions::err),
		FATAL(DistinctDamageDescriptions::fatal);

		private final Consumer<String> logger;

		private LogLevel(Consumer<String> logger) {
			this.logger = logger;
		}

		void logError(String msg) {
			this.logger.accept(msg);
		}
	}

	/**
	 * UID
	 */
	private static final long serialVersionUID = -677658162411281125L;

	private final LogLevel level;

	protected DDDConfigReaderException(String msg, LogLevel level) {
		super(msg);
		this.level = level;
	}
	
	protected DDDConfigReaderException(Exception cause, LogLevel level) {
		super(cause.getLocalizedMessage(), cause);
		this.level = level;
	}

	public void log() {
		this.level.logError(this.getLocalizedMessage());
	}
}
