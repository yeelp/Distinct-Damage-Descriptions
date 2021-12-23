package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public class ParrotPoisonDist extends AbstractSingleTypeDist {
	
	private static final HashSet<UUID> poisoned = new HashSet<UUID>();
	public ParrotPoisonDist() {
		super(() -> ModConfig.dmg.extraDamage.enableParrotPoisonDamage);
	}

	@Override
	public String getName() {
		return "parrotPoison";
	}

	@Override
	protected DDDDamageType getType() {
		return DDDBuiltInDamageType.POISON;
	}

	@Override
	protected boolean useType(DamageSource source, EntityLivingBase target) {
		return poisoned.contains(target.getUniqueID());
	}

	@Override
	public int priority() {
		return 1;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onParrotAttacked(LivingAttackEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		if(entity instanceof EntityParrot) {
			DamageSource src = evt.getSource();
			float amount = evt.getAmount();
			if(src.damageType.equals("player") && amount == Float.MAX_VALUE) {
				PotionEffect effect = entity.getActivePotionEffect(MobEffects.POISON);
				if(effect != null) {
					int dur = effect.getDuration();
					if(850 <= dur && dur <= 900) {
						poisoned.add(entity.getUniqueID());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onParrotDeath(LivingDeathEvent evt) {
		poisoned.remove(evt.getEntityLiving().getUniqueID());
	}
}
