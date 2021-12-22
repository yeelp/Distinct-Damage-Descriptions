package yeelp.distinctdamagedescriptions.potion;

import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;

@Mod.EventBusSubscriber(modid = ModConsts.MODID)
public final class DDDPotion extends Potion {

	private final DDDDamageType type;
	private final EffectType effect;

	public enum EffectType {
		GOOD("resistance"),
		BAD("vulnerability");

		private String effect;

		private EffectType(String s) {
			this.effect = s;
		}

		public String getEffect() {
			return this.effect;
		}
	}

	public DDDPotion(EffectType effect, DDDDamageType type) {
		super(effect == EffectType.BAD, type.getColour());
		this.type = type;
		this.effect = effect;
		String name = this.type.getTypeName() + "." + this.effect.getEffect();
		this.setRegistryName(name);
		this.setPotionName("effect." + name);
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		super.renderInventoryEffect(effect, gui, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		super.renderHUDEffect(effect, gui, x, y, z, alpha);
	}

	public DDDDamageType getType() {
		return this.type;
	}

	public EffectType getEffect() {
		return this.effect;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onGatherDefenses(GatherDefensesEvent evt) {
		evt.getDefender().getActivePotionEffects().stream().filter((effect) -> effect.getPotion() instanceof DDDPotion).forEach((effect) -> {
			DDDPotion pot = (DDDPotion) effect.getPotion();
			DDDDamageType type = pot.getType();
			float mod = 0.1f * (effect.getAmplifier() + 1);
			if(pot.getEffect() == EffectType.BAD) {
				mod *= -1;
			}
			evt.setResistance(type, evt.getResistance(type) + mod);
		});
	}
}
