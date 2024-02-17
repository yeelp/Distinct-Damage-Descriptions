package yeelp.distinctdamagedescriptions.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.ICreatureType;
import yeelp.distinctdamagedescriptions.event.DDDHooks;

public class MobHandler extends Handler {
	
	private static final String DDD_FIRST_LOAD = "ddd_first_load";
	
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
			DDDAPI.accessor.getMobCreatureType((EntityLivingBase) evt.getTarget()).filter(ICreatureType::isImmuneToCriticalHits).ifPresent((type) -> evt.setResult(Result.DENY));
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onMobSpawn(EntityJoinWorldEvent evt) {
		Entity entity = evt.getEntity();
		if(!(entity instanceof EntityLivingBase)) {
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		if(tag.hasKey(DDD_FIRST_LOAD)) {
			return;
		}
		EntityLivingBase livingEntity = (EntityLivingBase) entity;
		DDDAPI.accessor.getMobResistances(livingEntity).ifPresent((mobResists) -> {
			DDDHooks.fireAssignMobResistances(livingEntity, entity.world, mobResists);			
			tag.setByte(DDD_FIRST_LOAD, (byte) 1);
		});
	}
}
