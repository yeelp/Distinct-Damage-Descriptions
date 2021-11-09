package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import slimeknights.tconstruct.library.tools.ToolCore;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.capability.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;

public abstract class AbstractTinkersFormatter extends AbstractModCompatTooltipFormatter implements IModCompatTooltipFormatter<ItemStack> {

	protected <D extends IDistribution, C extends AbstractTinkersDistribution<D, ?>> AbstractTinkersFormatter(KeyTooltip keyTooltip, Capability<C> cap, String typeTextKey) {
		super(keyTooltip, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, cap, typeTextKey);
	}
	
	protected static final boolean isTinkerTool(ItemStack stack) {
		return stack.getItem() instanceof ToolCore;
	}
}
