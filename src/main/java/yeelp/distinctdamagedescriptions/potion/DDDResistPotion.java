package yeelp.distinctdamagedescriptions.potion;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DDDResistPotion extends AbstractDDDPotion {

	public DDDResistPotion(EffectType effect, DDDDamageType type) {
		super(effect, type, "resist");
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onGatherDefenses(GatherDefensesEvent evt) {
		evt.getDefender().getActivePotionEffects().stream().filter((effect) -> effect.getPotion() instanceof DDDResistPotion).forEach((effect) -> {
			DDDResistPotion pot = (DDDResistPotion) effect.getPotion();
			DDDDamageType type = pot.getType();
			float mod = 0.1f * (effect.getAmplifier() + 1);
			if(pot.getEffect() == EffectType.BAD) {
				mod *= -1;
			}
			evt.setResistance(type, evt.getResistance(type) + mod);
		});
	}
}
