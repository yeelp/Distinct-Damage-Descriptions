package yeelp.distinctdamagedescriptions.integration.client;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipMaker;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public abstract class AbstractModCompatItemDamageTooltipFormatterWrapper extends AbstractModCompatTooltipFormatterWrapper<ItemStack> {


	protected AbstractModCompatItemDamageTooltipFormatterWrapper() {
		super(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance());
	}
	
	@Override
	public final boolean applicable(ItemStack t) {
		return YResources.getRegistryStringWithMetadata(t).filter((s) -> TooltipMaker.ITEM.isApplicable(t, s) && this.checkForSpecificApplicability(t)).isPresent();
	}
	
	protected abstract boolean checkForSpecificApplicability(ItemStack t);

}
