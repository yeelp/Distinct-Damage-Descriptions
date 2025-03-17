package yeelp.distinctdamagedescriptions.util.development;

import java.util.function.Supplier;

import net.minecraft.world.World;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;
import yeelp.distinctdamagedescriptions.event.calculation.DDDCalculationEvent;

public abstract class AbstractDDDCalcEventDeveloperModeInfo<E extends DDDCalculationEvent> extends AbstractDeveloperModeInfo<E> {

	protected AbstractDDDCalcEventDeveloperModeInfo(Supplier<DeveloperStatus> sup) {
		super(sup);
	}

	@Override
	public final boolean shouldFire(E evt) {
		return true;
	}

	@Override
	public final World getWorld(E evt) {
		return evt.getDefender().world;
	}
}
