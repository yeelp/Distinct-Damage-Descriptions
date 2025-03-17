package yeelp.distinctdamagedescriptions.util.development;

import java.util.function.Supplier;

import net.minecraft.world.World;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.event.classification.DDDClassificationEvent;

public abstract class AbstractDDDClassificationEventDeveloperModeInfo<E extends DDDClassificationEvent> extends AbstractDeveloperModeInfo<E> {

	protected AbstractDDDClassificationEventDeveloperModeInfo(Supplier<DeveloperStatus> sup) {
		super(sup);
	}
	
	@Override
	public final World getWorld(E evt) {
		return evt.getDefender().world;
	}
	
	@Override
	public final boolean shouldFire(E evt) {
		return true;
	}

}
