package yeelp.distinctdamagedescriptions.util.development;

import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import yeelp.distinctdamagedescriptions.handlers.Handler;

abstract class DeveloperModeFeature {
	
	abstract Iterable<Handler> getCallbackHandlers();
	
	abstract boolean enabled();
	
	abstract void enable();
	
	abstract void disable();
	
	@Nonnull
	protected static <T, U> U mapIfNonNullElseGetDefault(T t, @Nonnull Function<T, U> f, @Nonnull U backup) {
		if(t != null) {
			return Objects.requireNonNull(f, "Function to apply can't be null!").apply(t);
		}
		return Objects.requireNonNull(backup, "backup can't be null!");
	}
}
