package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import c4.conarm.lib.armor.ArmorPart;
import c4.conarm.lib.materials.ArmorMaterialType;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.DistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;

public final class ConarmPlatesFormatter extends AbstractCapabilityTooltipFormatter<IArmorDistribution, ItemStack> implements IModCompatTooltipFormatter<ItemStack> {

	private static ConarmPlatesFormatter instance;

	protected ConarmPlatesFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, ConarmPlatesFormatter::getArmorDistribution, "armorresistances");

	}

	public static ConarmPlatesFormatter getInstance() {
		return instance == null ? instance = new ConarmPlatesFormatter() : instance;
	}

	private static final Optional<IArmorDistribution> getArmorDistribution(ItemStack stack) {
		if(stack.getItem() instanceof ArmorPart) {
			return Optional.of(TiCConfigurations.armorMaterialDist.getOrFallbackToDefault(((ArmorPart) stack.getItem()).getMaterialID(stack)));
		}
		return Optional.empty();
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
		return TooltipOrder.ARMOR;
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ArmorPart && ((ArmorPart) t.getItem()).hasUseForStat(ArmorMaterialType.PLATES);
	}

	@Override
	public IconAggregator getIconAggregator() {
		return ConarmPlatesIconAggregator.getInstance();
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack t, IArmorDistribution cap) {
		if(t == null || cap == null) {
			return Optional.empty();
		}
		return Optional.of(cap.getCategories().stream().sorted().map((type) -> TooltipTypeFormatter.ARMOR.format(type, cap.getWeight(type), this)).collect(Collectors.toList()));
	}

	protected static final class ConarmPlatesIconAggregator extends DistributionIconAggregator<IArmorDistribution> {

		private static ConarmPlatesIconAggregator instance;

		@SuppressWarnings("synthetic-access")
		protected ConarmPlatesIconAggregator() {
			super(ConarmPlatesFormatter.getInstance(), ConarmPlatesFormatter::getArmorDistribution);
		}

		public static ConarmPlatesIconAggregator getInstance() {
			return instance == null ? instance = new ConarmPlatesFormatter.ConarmPlatesIconAggregator() : instance;
		}

	}
}
