package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;

public final class CTEventHandler extends Handler implements IModIntegration {

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDetermineDamage(DetermineDamageEvent evt) {
		CTDDDEventManager.DETERMINE_DAMAGE.publish(new CTDetermineDamageEvent(evt));
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGatherDefenses(GatherDefensesEvent evt) {
		CTDDDEventManager.GATHER_DEFENSES.publish(new CTGatherDefensesEvent(evt));
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onShieldBlock(ShieldBlockEvent evt) {
		CTDDDEventManager.SHIELD_BLOCK.publish(new CTShieldBlockEvent(evt));
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUpdateAdpativeResistances(UpdateAdaptiveResistanceEvent evt) {
		CTDDDEventManager.UPDATE_ADAPTIVE.publish(new CTUpdateAdaptiveResistancesEvent(evt));
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.CRAFTTWEAKER_ID;
	}

	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.CRAFTWEAKER_TITLE;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(this);
	}
}