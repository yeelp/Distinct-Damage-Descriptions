package yeelp.distinctdamagedescriptions.registries.impl;

import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.handlers.AbstractTracker;
import yeelp.distinctdamagedescriptions.handlers.DaylightTracker;
import yeelp.distinctdamagedescriptions.registries.IDDDTrackersRegistry;

public final class DDDTrackers extends DDDBaseRegistry<AbstractTracker> implements IDDDTrackersRegistry {

	public DDDTrackers() {
		super(AbstractTracker::getName, "tracker");
	}

	@Override
	public void init() {
		this.register(false, new DaylightTracker());
		DDDConfigLoader.getInstance().enqueue(DaylightTracker.READER);
	}

	@Override
	public void register(boolean suppressOutput, AbstractTracker obj) {
		obj.register();
		super.register(suppressOutput, obj);
	}
}
