package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

/**
 * The singleton instance for formatting mob resistances
 * @author yeelp
 *
 */
public class MobResistancesFormatter extends AbstractCapabilityTooltipFormatter<MobResistanceCategories, ItemStack> {

	private static MobResistancesFormatter instance;

	private static final Style WHITE = new Style().setColor(TextFormatting.WHITE);

	private final ITextComponent notGenerated = super.getComponentWithStyle("notgenerated", new Style().setColor(TextFormatting.GOLD).setBold(true)),
								 noResists = super.getComponentWithStyle("noresists", WHITE);
	
	private MobResistancesFormatter() {
		this(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED);
	}
	
	protected MobResistancesFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter) {
		super(keyTooltip, numberFormatter, damageFormatter, (s) -> s == null ? null : DDDConfigurations.mobResists.get(Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(s)).map(ResourceLocation::toString).orElse("")), "mobresistances");
	}

	/**
	 * Get the singleton instance of this formatter if it exists, or create a new one if it doesn't
	 * @return The singleton instance if it exists, or a new instance if it doesn't.
	 */
	public static MobResistancesFormatter getInstance() {
		return instance == null ? instance = new MobResistancesFormatter() : instance;
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
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, MobResistanceCategories cap) {
		if(stack == null && cap == null) {
			return Optional.empty();
		}
		if(cap == null && ModConfig.generateStats) {
			return Optional.of(ImmutableList.of(this.notGenerated.getFormattedText()));
		}
		else if (cap != null) {
			List<String> lst = cap.getResistanceMap().entrySet().stream().sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).collect(LinkedList<String>::new, (l, e) -> l.add(TooltipTypeFormatter.MOB_RESISTS.format(e.getKey(), e.getValue(), this)), LinkedList<String>::addAll);
			if(lst.isEmpty()) {
				lst.add(this.noResists.getFormattedText());
			}
			TooltipTypeFormatter.MOB_RESISTS.formatImmunities(cap.getImmunities()).ifPresent(lst::add);
			lst.addAll(TooltipTypeFormatter.MOB_RESISTS.formatAdaptability(cap.adaptiveChance(), cap.getAdaptiveAmount()));
			return Optional.of(lst);
		}
		return Optional.of(ImmutableList.of(this.noResists.getFormattedText()));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_RESISTANCES;
	}
	
}
