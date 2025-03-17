package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatTooltipFormatterWrapper;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class SpellcastingItemDamageDistributionFormatter extends AbstractModCompatTooltipFormatterWrapper<ItemStack> {

	private static SpellcastingItemDamageDistributionFormatter instance;

	private SpellcastingItemDamageDistributionFormatter() {
		super(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance());
	}

	@Override
	public boolean applicable(ItemStack t) {
		return SpellDistributionItemFormatter.getInstance().applicable(t);
	}

	public static SpellcastingItemDamageDistributionFormatter getInstance() {
		return instance == null ? new SpellcastingItemDamageDistributionFormatter() : instance;
	}
}
