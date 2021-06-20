package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

/**
 * The singleton instance for formatting mob resistances
 * @author yeelp
 *
 */
public class MobResistancesFormatter extends AbstractCapabilityTooltipFormatter<MobResistanceCategories> {

	private static MobResistancesFormatter instance;

	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY),
							   WHITE = new Style().setColor(TextFormatting.WHITE), 
							   AQUA = new Style().setColor(TextFormatting.AQUA),
							   LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE),
							   DARK_RED = new Style().setColor(TextFormatting.DARK_RED);

	private final ITextComponent resistanceSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.resistance").setStyle(GRAY),
								 weaknessSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.weakness").setStyle(DARK_RED),
								 notGenerated = new TextComponentTranslation("tooltips.distinctdamagedescriptions.notgenerated").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true)),
								 noResists = new TextComponentTranslation("tooltips.distinctdamagedescriptions.noresists").setStyle(WHITE),
								 immunityPrefix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.immunities").setStyle(AQUA),
								 adaptabilityPrefix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptivechance").setStyle(LIGHT_PURPLE),
								 adaptabilityAmountPrefix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptiveamount").setStyle(LIGHT_PURPLE);
	
	private MobResistancesFormatter() {
		this(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED);
	}
	
	protected MobResistancesFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter) {
		super(keyTooltip, numberFormatter, damageFormatter, (s) -> s == null ? null : DDDConfigurations.mobResists.get(Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(s)).map(ResourceLocation::toString).orElse("")), new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobresistances").setStyle(GRAY));
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
			List<String> lst = cap.getResistanceMap().entrySet().stream().sorted(Comparator.<Entry<DDDDamageType, Float>>comparingDouble(Entry::getValue).thenComparing(Entry::getKey)).collect(LinkedList<String>::new, (l, e) -> l.add(makeOneMobResistString(e.getValue(), e.getKey())), LinkedList<String>::addAll);
			if(lst.isEmpty()) {
				lst.add(this.noResists.getFormattedText());
			}
			makeImmunityString(cap.getImmunities()).ifPresent(lst::add);
			lst.addAll(makeAdaptiveString(cap.adaptiveChance(), cap.getAdaptiveAmount()));
			return Optional.of(lst);
		}
		return Optional.of(ImmutableList.of(this.noResists.getFormattedText()));
	}
	
	private String makeOneMobResistString(float amount, DDDDamageType type) {
		boolean isNegative = amount < 0;
		return String.format("   %s%s %s %s", isNegative ? TextFormatting.DARK_RED.toString() : TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), isNegative ? this.weaknessSuffix.getFormattedText() : this.resistanceSuffix.getFormattedText());
	}
	
	private Optional<String> makeImmunityString(Set<DDDDamageType> immunities) {
		if(immunities.isEmpty()) {
			return Optional.empty();
		}
		String str = this.immunityPrefix.getFormattedText() + " ";
		String[] strings = new String[immunities.size()];
		strings = immunities.stream().sorted().map(DDDDamageFormatter.COLOURED::format).collect(Collectors.toList()).toArray(strings);
		return Optional.of(str + YLib.joinNiceString(true, ",", strings));
	}
	
	private List<String> makeAdaptiveString(float chance, float amount) {
		if(chance == 0 && amount == 0) {
			return Collections.emptyList();
		}
		String str1 = String.format("%s %s", this.adaptabilityPrefix.getFormattedText(), this.getNumberFormatter().format(chance));
		String str2 = String.format("   %s %s", this.adaptabilityAmountPrefix.getFormattedText(), this.getNumberFormatter().format(amount));
		return ImmutableList.of(str1, str2);
	}
	
}
