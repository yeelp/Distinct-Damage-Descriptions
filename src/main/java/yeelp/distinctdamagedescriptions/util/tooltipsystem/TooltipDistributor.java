package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.Icon;

public enum TooltipDistributor {
	BASE {
		@Override
		public List<String> getTooltip(ItemStack stack) {
			return TooltipMaker.makeTooltipStrings(stack);
		}

		@Override
		public List<Icon> getIcons(ItemStack stack, int x, int y, List<String> lines) {
			return TooltipMaker.getIconsFor(stack, x, y, lines);
		}

		@Override
		protected boolean applicable(ItemStack stack) {
			return true;
		}

		@Override
		protected void registerModCompatFormatter(IModCompatTooltipFormatter<ItemStack> formatter) {
			// no-op
		}
	},
	MOD_COMPAT {

		protected final List<IModCompatTooltipFormatter<ItemStack>> formatters = Lists.newArrayList();

		@Override
		public List<String> getTooltip(ItemStack stack) {
			return this.getApplicableFormatters(stack).map((f) -> f.format(stack)).reduce(this.listReduction()).orElse(Collections.emptyList());
		}

		@Override
		public List<Icon> getIcons(ItemStack stack, int x, int y, List<String> lines) {
			return this.getApplicableFormatters(stack).map((f) -> f.getIconAggregator().getIconsToDraw(stack, x, y, lines)).reduce(this.listReduction()).orElse(Collections.emptyList());
		}

		@Override
		protected boolean applicable(ItemStack stack) {
			return this.formatters.stream().anyMatch((f) -> f.applicable(stack));
		}

		private Stream<IModCompatTooltipFormatter<ItemStack>> getApplicableFormatters(ItemStack stack) {
			return this.formatters.stream().filter((f) -> f.applicable(stack)).sorted();
		}

		private final <T> BinaryOperator<List<T>> listReduction() {
			return (l1, l2) -> {
				l1.addAll(l2);
				return l1;
			};
		}

		@Override
		protected void registerModCompatFormatter(IModCompatTooltipFormatter<ItemStack> formatter) {
			this.formatters.add(formatter);
		}
	};

	public static TooltipDistributor getDistributor(ItemStack stack) {
		return MOD_COMPAT.applicable(stack) ? MOD_COMPAT : BASE;
	}

	public static void registerModCompat(IModCompatTooltipFormatter<ItemStack> formatter) {
		MOD_COMPAT.registerModCompatFormatter(formatter);
	}

	public abstract List<String> getTooltip(ItemStack stack);

	public abstract List<Icon> getIcons(ItemStack stack, int x, int y, List<String> lines);

	protected abstract boolean applicable(ItemStack stack);

	protected abstract void registerModCompatFormatter(IModCompatTooltipFormatter<ItemStack> formatter);
}
