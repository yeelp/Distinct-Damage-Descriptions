package yeelp.distinctdamagedescriptions.integration.hwyla;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.IDDDTooltipInjector.IArmorTooltipInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter.Armor;

public interface IHwylaTooltipInjectors {
	
	public interface IHwylaArmorTooltipInjector extends IArmorTooltipInjector {
		@Override
		default Armor getFormatterToUse() {
			return null;
		}
		
		@Override
		default boolean shouldUseFormatter(ItemStack stack) {
			return false;
		}
	}
}
