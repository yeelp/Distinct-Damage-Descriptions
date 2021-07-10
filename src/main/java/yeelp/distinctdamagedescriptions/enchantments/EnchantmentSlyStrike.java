package yeelp.distinctdamagedescriptions.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;

public class EnchantmentSlyStrike extends Enchantment {

	public EnchantmentSlyStrike() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("slystrike");
		this.setName(ModConsts.MODID + ".slystrike");
	}

	@Override
	public int getMinEnchantability(int level) {
		return 7 * level + 24;
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 8 * level + 35;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != DDDEnchantments.bruteForce;
	}
}
