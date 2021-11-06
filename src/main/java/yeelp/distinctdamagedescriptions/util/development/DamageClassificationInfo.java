package yeelp.distinctdamagedescriptions.util.development;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

final class DamageClassificationInfo extends DeveloperModeFeature {

	@Override
	Iterable<Handler> getCallbackHandlers() {
		return ImmutableList.of(new DamageClassificationHandler());
	}

	@Override
	boolean enabled() {
		return false;
	}

	@Override
	void enable() {
		// TODO Auto-generated method stub

	}

	@Override
	void disable() {
		// TODO Auto-generated method stub

	}

	protected final class DamageClassificationHandler extends Handler {

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onDamageClassify(DetermineDamageEvent evt) {
			if(enabled()) {
				DeveloperModeKernel.getInstance().log(String.format("%s was attacker with the following distribution: %s", evt.getDefender().getName(), DeveloperModeUtilities.damageTypeRegistryToString((t) -> evt.getDamage(t) > 0, (t) -> String.format("(%s, %f)", t.getDisplayName(), evt.getDamage(t)))), false);
			}
		}
	}

}
