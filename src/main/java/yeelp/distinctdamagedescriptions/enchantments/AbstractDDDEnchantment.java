package yeelp.distinctdamagedescriptions.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.ModConsts;

public abstract class AbstractDDDEnchantment extends Enchantment {

	protected AbstractDDDEnchantment(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setRegistryName(this.getEnchantmentNameInternal());
		this.setName(String.format("%s.%s", ModConsts.MODID, this.getEnchantmentNameInternal()));
	}
	
	protected abstract String getEnchantmentNameInternal();

}
