package yeelp.distinctdamagedescriptions.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DDDDamagePotion extends AbstractDDDPotion {

	public DDDDamagePotion(EffectType effect, DDDDamageType type) {
		super(effect, type, "damage");
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onDetermineDamage(DetermineDamageEvent evt) {
		Entity attacker = evt.getTrueAttacker();
		if(attacker instanceof EntityLivingBase) {
			((EntityLivingBase) attacker).getActivePotionEffects().stream().filter((effect) -> effect.getPotion() instanceof DDDDamagePotion).forEach((effect) -> {
				DDDDamagePotion pot = (DDDDamagePotion) effect.getPotion();
				DDDDamageType type = pot.getType();
				float mod = 0.1f * (effect.getAmplifier() + 1);
				if(pot.getEffect() == EffectType.BAD) {
					mod = 1 - mod;
				}
				else {
					mod += 1;
				}
				evt.setDamage(type, evt.getDamage(type)*mod);
			});
		}
	}

}
