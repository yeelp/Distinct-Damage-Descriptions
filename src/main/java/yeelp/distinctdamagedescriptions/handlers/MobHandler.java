package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;

public class MobHandler extends Handler {
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onPotionApplyEvent(PotionEvent.PotionApplicableEvent evt) {
		DDDAPI.accessor.getMobCreatureType(evt.getEntityLiving()).filter((type) -> type.isImmuneToPotionEffect(evt.getPotionEffect())).ifPresent((type) -> evt.setResult(Result.DENY));
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onCrit(CriticalHitEvent evt) {
		Entity target = evt.getTarget();
		if(target instanceof EntityLivingBase) {
			DDDAPI.accessor.getMobCreatureType((EntityLivingBase) evt.getTarget()).filter(IMobCreatureType::isImmuneToCriticalHits).ifPresent((type) -> evt.setResult(Result.DENY));
		}
	}
}
