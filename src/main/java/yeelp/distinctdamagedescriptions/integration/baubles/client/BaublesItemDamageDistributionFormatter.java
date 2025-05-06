package yeelp.distinctdamagedescriptions.integration.baubles.client;

import baubles.api.IBauble;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatTooltipFormatterWrapper;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class BaublesItemDamageDistributionFormatter extends AbstractModCompatTooltipFormatterWrapper<ItemStack> {

	private static BaublesItemDamageDistributionFormatter instance;
	
	private BaublesItemDamageDistributionFormatter() {
		super(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance());
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof IBauble;
	}
	
	public static BaublesItemDamageDistributionFormatter getInstance() {
		return instance == null ? new BaublesItemDamageDistributionFormatter() : instance;
	}
}
