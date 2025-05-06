package yeelp.distinctdamagedescriptions.integration.thaumcraft.client;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatTooltipFormatterWrapper;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class FocusItemDamageDistributionFormatter extends AbstractModCompatTooltipFormatterWrapper<ItemStack> {

	private static FocusItemDamageDistributionFormatter instance;

	protected FocusItemDamageDistributionFormatter() {
		super(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance());
	}
	
	@Override
	public boolean applicable(ItemStack t) {
		return FocusDistributionItemFormatter.getInstance().applicable(t);
	}
	
	public static FocusItemDamageDistributionFormatter getInstance() {
		return instance == null ? instance = new FocusItemDamageDistributionFormatter() : instance;
	}

}
