package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.MobResistanceCategories;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * The singleton instance for formatting mob resistances
 * 
 * @author Yeelp
 *
 */
public class MobResistancesFormatter extends AbstractCapabilityTooltipFormatter<MobResistanceCategories, ItemStack> {

	private static MobResistancesFormatter instance;

	private final ITextComponent notGenerated = getComponentWithStyle("notgenerated", new Style().setColor(TextFormatting.GOLD).setBold(true));

	private MobResistancesFormatter() {
		this(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED);
	}

	protected MobResistancesFormatter(KeyTooltip keyTooltip, DDDDamageFormatter damageFormatter, Function<ItemStack, Optional<ResourceLocation>> f) {
		super(keyTooltip, damageFormatter, (s) -> f.apply(s).flatMap(TooltipFormatterUtilities::getMobResistancesIfConfigured), "mobresistances");
	}

	protected MobResistancesFormatter(@SuppressWarnings("SameParameterValue") KeyTooltip keyTooltip, @SuppressWarnings("SameParameterValue") DDDDamageFormatter damageFormatter) {
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
	protected Optional<List<String>> formatCapabilityFor(@Nonnull ItemStack stack, @Nonnull MobResistanceCategories cap) {
		if(cap == MobResistanceCategories.EMPTY) {
			return Optional.empty();
		}
		if(ModConfig.core.generateStats) {
			return Optional.of(ImmutableList.of(this.notGenerated.getFormattedText()));
		}
		List<String> lst = cap.getResistanceMap().entrySet().stream().filter((e) -> !e.getKey().isHidden() && e.getValue() != 0).sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).collect(LinkedList::new, (l, e) -> l.add(TooltipTypeFormatter.MOB_RESISTS.format(e.getKey(), e.getValue(), this)), LinkedList::addAll);
		if(lst.isEmpty()) {
			lst.add(AbstractCapabilityTooltipFormatter.NONE_TEXT.getFormattedText());
		}
		Set<DDDDamageType> immunities = cap.getImmunities();
		immunities.removeIf(DDDDamageType::isHidden);
		TooltipTypeFormatter.MOB_RESISTS.formatImmunities(immunities).ifPresent(lst::add);
		lst.addAll(TooltipTypeFormatter.MOB_RESISTS.formatAdaptability(cap.adaptiveChance(), cap.getAdaptiveAmount()));
		return Optional.of(lst);
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
