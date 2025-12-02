package yeelp.distinctdamagedescriptions.integration.baubles.client;

import baubles.api.cap.BaublesCapabilities;
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
		return t.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
	}
	
	public static BaublesItemDamageDistributionFormatter getInstance() {
		return instance == null ? new BaublesItemDamageDistributionFormatter() : instance;
	}
}
