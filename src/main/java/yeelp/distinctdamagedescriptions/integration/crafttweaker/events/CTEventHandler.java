package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.event.DamageCalculationEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class CTEventHandler extends Handler implements IModIntegration {
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void preDamage(DamageCalculationEvent.Pre evt) {
		CTDDDEventManager.PRE_DAMAGE.publish(new CTPreDamageEvent(evt));
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void postDamage(DamageCalculationEvent.Post evt) {
		CTDDDEventManager.POST_DAMAGE.publish(new CTPostDamageEvent(evt));
	}

	@Override
	public String getModID() {
		return ModConsts.CRAFTTWEAKER_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(this);
	}
}