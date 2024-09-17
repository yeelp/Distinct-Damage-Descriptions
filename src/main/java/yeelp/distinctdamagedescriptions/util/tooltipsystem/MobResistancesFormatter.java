package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.MobResistanceCategories;

/**
 * The singleton instance for formatting mob resistances
 * 
 * @author Yeelp
 *
 */
public class MobResistancesFormatter extends AbstractCapabilityTooltipFormatter<MobResistanceCategories, ItemStack> {

	private static MobResistancesFormatter instance;

	private final ITextComponent notGenerated = super.getComponentWithStyle("notgenerated", new Style().setColor(TextFormatting.GOLD).setBold(true));

	private MobResistancesFormatter() {
		this(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED);
	}

	protected MobResistancesFormatter(KeyTooltip keyTooltip, DDDDamageFormatter damageFormatter, Function<ItemStack, Optional<ResourceLocation>> f) {
		super(keyTooltip, damageFormatter, (s) -> f.apply(s).flatMap(TooltipFormatterUtilities::getMobResistancesIfConfigured), "mobresistances");
	}

	protected MobResistancesFormatter(KeyTooltip keyTooltip, DDDDamageFormatter damageFormatter) {
		this(keyTooltip, damageFormatter, TooltipFormatterUtilities::getResourceLocationFromSpawnEgg);
	}

	/**
	 * Get the singleton instance of this formatter if it exists, or create a new
	 * one if it doesn't
	 * 
	 * @return The singleton instance if it exists, or a new instance if it doesn't.
	 */
	public static MobResistancesFormatter getInstance() {
		return instance == null ? instance = new MobResistancesFormatter() : instance;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, MobResistanceCategories cap) {
		if(stack == null && cap == MobResistanceCategories.EMPTY) {
			return Optional.empty();
		}
		if(cap == MobResistanceCategories.EMPTY && ModConfig.core.generateStats) {
			return Optional.of(ImmutableList.of(this.notGenerated.getFormattedText()));
		}
		else if(cap != null) {
			List<String> lst = cap.getResistanceMap().entrySet().stream().filter((e) -> !e.getKey().isHidden() && e.getValue() != 0).sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).collect(LinkedList<String>::new, (l, e) -> l.add(TooltipTypeFormatter.MOB_RESISTS.format(e.getKey(), e.getValue(), this)), LinkedList<String>::addAll);
			if(lst.isEmpty()) {
				lst.add(AbstractCapabilityTooltipFormatter.NONE_TEXT.getFormattedText());
			}
			Set<DDDDamageType> immunities = cap.getImmunities();
			immunities.removeIf(DDDDamageType::isHidden);
			TooltipTypeFormatter.MOB_RESISTS.formatImmunities(immunities).ifPresent(lst::add);
			lst.addAll(TooltipTypeFormatter.MOB_RESISTS.formatAdaptability(cap.adaptiveChance(), cap.getAdaptiveAmount()));
			return Optional.of(lst);
		}
		return Optional.of(ImmutableList.of(AbstractCapabilityTooltipFormatter.NONE_TEXT.getFormattedText()));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_RESISTANCES;
	}

	@Override
	public DDDTooltipColourScheme getColourScheme() {
		return ModConfig.client.mobResistColourScheme;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}

}
