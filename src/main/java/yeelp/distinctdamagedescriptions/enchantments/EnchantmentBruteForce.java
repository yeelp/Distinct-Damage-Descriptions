package yeelp.distinctdamagedescriptions.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;

/**
 * The Brute Force enchantment. Reduces resistances by 10% per level
 * 
 * @author Yeelp
 *
 */
public class EnchantmentBruteForce extends Enchantment {

	public EnchantmentBruteForce() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		this.setRegistryName("bruteforce");
		this.setName(ModConsts.MODID + ".bruteforce");
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
		return 5;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != DDDEnchantments.slyStrike;
	}
}
