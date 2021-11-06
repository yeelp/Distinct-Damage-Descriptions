package yeelp.distinctdamagedescriptions.util.development;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.handlers.Handler;

final class PreCalculationInfo extends DeveloperModeFeature {

	@Override
	Iterable<Handler> getCallbackHandlers() {
		return ImmutableList.of(new LivingAttackHandler());
	}

	@Override
	boolean enabled() {
		// TODO Auto-generated method stub
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
	
	protected final class LivingAttackHandler extends Handler {
		
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onAttack(LivingAttackEvent evt) {
			if(enabled()) {
				DamageSource src = evt.getSource();
				String attacker = DeveloperModeFeature.mapIfNonNullElseGetDefault(src.getImmediateSource(), Entity::getName, "No direct attacker");
				String trueAttacker = DeveloperModeFeature.mapIfNonNullElseGetDefault(src.getTrueSource(), Entity::getName, "No attacker");
				DeveloperModeKernel.getInstance().log(String.format("Attacker: %s, True Attacker: %s, Defender: %s, Source Name: %s", attacker, trueAttacker, evt.getEntityLiving().getName(), src.damageType), false);
			}
		}
	}

}
