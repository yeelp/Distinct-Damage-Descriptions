package yeelp.distinctdamagedescriptions.util.development;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public abstract class AbstractDeveloperModeInfo<E extends Event> implements IDeveloperModeInfoCallback<E> {

	private final Supplier<DeveloperStatus> status;
	protected static final String NEW_LINE = System.lineSeparator();
	
	protected AbstractDeveloperModeInfo(Supplier<DeveloperStatus> sup) {
		this.status = sup;
	}
	
	@Override
	public DeveloperStatus getStatus() {
		return this.status.get();
	}
	
	@Override
	public final String callback(E evt) {
		return this.getInfo(evt, new StringBuilder()).toString();
	}
	
	protected abstract StringBuilder getInfo(E evt, StringBuilder sb);
	
	@Nonnull
	protected static final <T, U> U mapIfNonNullElseGetDefault(T t, @Nonnull Function<T, U> f, @Nonnull U backup) {
		if(t != null) {
			return Objects.requireNonNull(f, "Function to apply can't be null!").apply(t);
		}
		return Objects.requireNonNull(backup, "backup can't be null!");
	}
	
	@Nonnull
	protected static final String getEntityNameAndID(@Nonnull Entity entity) {
		return String.format("%s (%s)", entity.getName(), YResources.getEntityIDString(entity).orElse("No Entity ID found!"));
	}
}
