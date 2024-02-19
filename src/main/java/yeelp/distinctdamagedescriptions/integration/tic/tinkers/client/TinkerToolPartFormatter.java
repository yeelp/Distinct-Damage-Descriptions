package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tools.ToolPart;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.AbstractCapabilityIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.Icon;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public class TinkerToolPartFormatter extends AbstractCapabilityTooltipFormatter<DistributionBias, ItemStack> implements IModCompatTooltipFormatter<ItemStack> {

	private static TinkerToolPartFormatter instance;
	private final BiasFormatter biasFormatter = new BiasFormatter();
	private final BiasIconAggregator biasAggregator = new BiasIconAggregator(this.biasFormatter.getStart());

	private TinkerToolPartFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, TinkerToolPartFormatter::getDistributionBias, "tic.distributionbias");
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f == DDDNumberFormatter.PERCENT;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.DAMAGE;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack t, DistributionBias cap) {
		if(t == null || cap == null) {
			return Optional.empty();
		}
		List<String> lst = cap.getPreferredMapCopy().entrySet().stream().sorted(Comparator.comparing(Entry<DDDDamageType, Float>::getKey).thenComparing(Entry::getValue)).map((e) -> TooltipTypeFormatter.DEFAULT_DAMAGE.format(e.getKey(), e.getValue(), this)).collect(Collectors.toList());
		lst.add(0, this.biasFormatter.format(null, cap.getBias(), this));
		return Optional.of(lst);
	}

	@Override
	public List<String> format(ItemStack t) {
		List<String> damageDistTip = ItemDistributionFormatter.getInstance().format(t);
		damageDistTip.addAll(super.format(t));
		return damageDistTip;
	}

	public static TinkerToolPartFormatter getInstance() {
		return instance == null ? instance = new TinkerToolPartFormatter() : instance;
	}

	protected static Optional<DistributionBias> getDistributionBias(ItemStack stack) {
		if(stack.getItem() instanceof ToolPart) {
			ToolPart part = (ToolPart) stack.getItem();
			return Optional.of(TiCConfigurations.toolMaterialBias.getOrFallbackToDefault(part.getMaterialID(stack)));
		}
		return Optional.empty();
	}

	private final class BiasFormatter extends TooltipTypeFormatter {
		BiasFormatter() {
			super("tic.influence");
			this.suffix.setStyle(new Style().setColor(TextFormatting.WHITE).setBold(true));
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
			return this.getStart().concat(" " + TinkerToolPartFormatter.super.getNumberFormatter().format(amount));
		}

		public String getStart() {
			return this.suffix.getFormattedText();
		}
	}

	private final class BiasIconAggregator extends AbstractCapabilityIconAggregator implements IconAggregator {

		BiasIconAggregator(String regex) {
			super(TextFormatting.GRAY.toString() + regex, () -> TinkerToolPartFormatter.getInstance().shouldShow());
		}

		@Override
		protected Stream<DDDDamageType> getOrderedTypes(ItemStack stack) {
			return TinkerToolPartFormatter.getDistributionBias(stack).map((bias) -> bias.getPreferredMapCopy().keySet().stream().sorted()).orElse(Stream.empty());
		}

		@Override
		public List<Icon> getIconsToDraw(ItemStack stack, int x, int y, List<String> tooltips) {
			List<Icon> lst = new ArrayList<Icon>();
			lst.addAll(ItemDamageDistributionIconAggregator.getInstance().getIconsToDraw(stack, x, y, tooltips));
			lst.addAll(super.getIconsToDraw(stack, x, y, tooltips));
			return lst;
		}
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ToolPart && ((ToolPart) t.getItem()).hasUseForStat(MaterialTypes.HEAD);
	}

	@Override
	public IconAggregator getIconAggregator() {
		return this.biasAggregator;
	}
}
