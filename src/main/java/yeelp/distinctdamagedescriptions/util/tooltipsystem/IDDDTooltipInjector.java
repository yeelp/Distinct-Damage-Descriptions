package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.util.IPriority;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;

public interface IDDDTooltipInjector extends IPriority {
	
	public interface IArmorTooltipInjector extends IDDDTooltipInjector {
		
		boolean applies(ItemStack stack);
		
		ArmorValues alterArmorValues(ItemStack stack, float armor, float toughness);
		
		boolean shouldUseFormatter(ItemStack stack);
		
		TooltipTypeFormatter.Armor getFormatterToUse();
	}

}
