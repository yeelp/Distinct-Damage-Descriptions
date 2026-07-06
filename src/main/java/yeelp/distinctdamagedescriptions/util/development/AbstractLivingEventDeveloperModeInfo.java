package yeelp.distinctdamagedescriptions.util.development;

import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import yeelp.distinctdamagedescriptions.config.dev.DevelopmentCategory.DeveloperStatus;

import java.util.function.Supplier;

public abstract class AbstractLivingEventDeveloperModeInfo<E extends LivingEvent> extends AbstractDeveloperModeInfo<E> {

	protected AbstractLivingEventDeveloperModeInfo(Supplier<DeveloperStatus> sup) {
		super(sup);
	}

	@Override
	public World getWorld(E evt) {
		return evt.getEntityLiving().world;
	}
}
