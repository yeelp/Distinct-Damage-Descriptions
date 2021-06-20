package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ParrotPoisonDamageHandler extends Handler {
	private final HashSet<UUID> poisoned = new HashSet<UUID>();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onParrotAttacked(LivingAttackEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		if(entity instanceof EntityParrot) {
			DamageSource src = evt.getSource();
			float amount = evt.getAmount();
			if(src.damageType.equals("player") && amount == Float.MAX_VALUE) {
				PotionEffect effect = entity.getActivePotionEffect(MobEffects.POISON);
				if(effect != null) {
					int dur = effect.getDuration();
					if(850 <= dur && dur <= 900) {
						this.poisoned.add(entity.getUniqueID());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onParrotDeath(LivingDeathEvent evt) {
		this.poisoned.remove(evt.getEntityLiving().getUniqueID());
	}

	public boolean wasPoisonedByCookie(UUID id) {
		return this.poisoned.contains(id);
	}
}
