package yeelp.distinctdamagedescriptions.integration.baubles.client;

import java.util.List;

import baubles.api.IBauble;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class BaublesItemDamageDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static BaublesItemDamageDistributionFormatter instance;
	
	private BaublesItemDamageDistributionFormatter() {
		
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return ItemDistributionFormatter.getInstance().supportsDamageFormat(f);
	}

	@Override
	public void setDamageFormatter(DDDDamageFormatter f) {
		return;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return ItemDistributionFormatter.getInstance().getNumberFormattingStrategy();
	}

	@Override
	public List<String> format(ItemStack t) {
		return ItemDistributionFormatter.getInstance().format(t);
	}

	@Override
	public TooltipOrder getType() {
		return ItemDistributionFormatter.getInstance().getType();
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof IBauble;
	}

	@Override
	public IconAggregator getIconAggregator() {
		return ItemDamageDistributionIconAggregator.getInstance();
	}
	
	public static BaublesItemDamageDistributionFormatter getInstance() {
		return instance == null ? new BaublesItemDamageDistributionFormatter() : instance;
	}
}
