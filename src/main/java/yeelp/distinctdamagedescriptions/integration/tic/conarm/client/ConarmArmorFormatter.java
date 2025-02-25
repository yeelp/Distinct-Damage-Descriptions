package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ArmorDistributionNumberFormat;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ArmorDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.Icon;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class ConarmArmorFormatter extends ArmorDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static ConarmArmorFormatter instance;

	@Override
	public List<String> format(ItemStack t) {
		List<String> tooltips = ItemDistributionFormatter.getInstance().format(t);
		tooltips.addAll(super.format(t));
		return tooltips;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, IArmorDistribution cap) {
		if(this.getNumberFormattingStrategy() == ArmorDistributionNumberFormat.PLAIN) {
			int slot = ((TinkersArmor) stack.getItem()).armorType.getIndex();
			return this.getArmorTooltip(stack, cap, ArmorHelper.getArmor(stack, slot), ArmorHelper.getToughness(stack));
		}
		return super.formatCapabilityFor(stack, cap);
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof TinkersArmor;
	}

	@Override
	public IconAggregator getIconAggregator() {
		return ConarmArmorIconAggregator.getInstance();
	}

	public static ConarmArmorFormatter getInstance() {
		return instance == null ? instance = new ConarmArmorFormatter() : instance;
	}

	private static final class ConarmArmorIconAggregator extends ArmorDistributionIconAggregator {
		private static ConarmArmorIconAggregator instance;

		public static ConarmArmorIconAggregator getInstance() {
			return instance == null ? instance = new ConarmArmorFormatter.ConarmArmorIconAggregator() : instance;
		}

		@Override
		public List<Icon> getIconsToDraw(ItemStack stack, int x, int y, List<String> tooltips) {
			List<Icon> icons = new ArrayList<Icon>();
			icons.addAll(ItemDamageDistributionIconAggregator.getInstance().getIconsToDraw(stack, x, y, tooltips));
			icons.addAll(super.getIconsToDraw(stack, x, y, tooltips));
			return icons;
		}

	}
}
