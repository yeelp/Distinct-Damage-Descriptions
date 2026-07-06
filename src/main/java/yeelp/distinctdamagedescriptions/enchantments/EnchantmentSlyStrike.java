package yeelp.distinctdamagedescriptions.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.init.DDDEnchantments;

import javax.annotation.Nonnull;

public class EnchantmentSlyStrike extends AbstractDDDEnchantment {

	public static final String NAME = "slystrike";
	
	public EnchantmentSlyStrike() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {
				EntityEquipmentSlot.MAINHAND});
	}
	
	@Override
	protected String getEnchantmentNameInternal() {
		return NAME;
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
	public boolean canApplyTogether(@Nonnull Enchantment ench) {
		return super.canApplyTogether(ench) && ench != DDDEnchantments.bruteForce;
	}
}
