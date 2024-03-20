package yeelp.distinctdamagedescriptions.potion;

import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;

public abstract class AbstractDDDPotion extends Potion {

	private DDDDamageType type;
	private final EffectType effect;

	public enum EffectType {
		GOOD("buff"),
		BAD("debuff");

		private String effect;

		private EffectType(String s) {
			this.effect = s;
		}

		public String getEffect() {
			return this.effect;
		}
	}
	
	protected AbstractDDDPotion(EffectType effect, DDDDamageType type, String root) {
		super(effect == EffectType.BAD, type.getColour());
		this.type = type;
		this.effect = effect;
		String name = String.format("%s.%s.%s", this.type.getTypeName(), root, this.effect.getEffect());
		this.setRegistryName(new ResourceLocation(ModConsts.MODID, name));
		this.setPotionName("effect."+name);
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
}
