package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.ResistMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public class HwylaMobResistanceFormatter extends HwylaTooltipFormatter<IMobResistances> {

	private static HwylaMobResistanceFormatter instance;

	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY),
			WHITE = new Style().setColor(TextFormatting.WHITE), AQUA = new Style().setColor(TextFormatting.AQUA),
			LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE),
			DARK_RED = new Style().setColor(TextFormatting.DARK_RED);

	private final ITextComponent resistanceSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.resistance").setStyle(GRAY),
			weaknessSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.weakness").setStyle(DARK_RED),
			noResists = new TextComponentTranslation("tooltips.distinctdamagedescriptions.noresists").setStyle(WHITE),
			immunityPrefix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.immunities").setStyle(AQUA),
			adaptabilityAmountPrefix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.adaptiveamount").setStyle(LIGHT_PURPLE);

	private HwylaMobResistanceFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.STANDARD, DDDAPI.accessor::getMobResistances, new TextComponentTranslation("tooltips.distinctdamagedescriptions.hwyla.mobresistances").setStyle(new Style().setColor(TextFormatting.GRAY)));
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static HwylaMobResistanceFormatter getInstance() {
		return instance == null ? instance = new HwylaMobResistanceFormatter() : instance;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f == DDDNumberFormatter.PERCENT;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(EntityLivingBase t, IMobResistances cap) {
		if(t == null && cap == null) {
			return Optional.empty();
		}
		List<String> result = new LinkedList<String>();
		Set<DDDDamageType> immunities = new HashSet<DDDDamageType>();
		ArmorMap aMap = DDDAPI.accessor.getArmorValuesForEntity(t);
		ResistMap rMap = DDDAPI.accessor.classifyResistances(new HashSet<DDDDamageType>(DDDRegistries.damageTypes.getAll()), cap);
		for(Iterator<DDDDamageType> it = DDDRegistries.damageTypes.getAll().stream().sorted(Comparator.<DDDDamageType>comparingDouble((d) -> rMap.get(d)).thenComparing(Comparator.naturalOrder())).iterator(); it.hasNext();) {
			DDDDamageType type = it.next();
			if(cap.hasImmunity(type)) {
				immunities.add(type);
			}
			if(aMap.containsKey(type)) {
				result.add(this.makeOneMobResistString(rMap.get(type), aMap.get(type), type));
			}
			else if (rMap.get(type) != 0) {
				result.add(this.makeOneMobResistString(rMap.get(type), type));
			}
		}
		if(result.isEmpty()) {
			result.add(this.noResists.getFormattedText());
		}
		makeImmunityString(immunities).ifPresent(result::add);
		if(cap.hasAdaptiveResistance()) {
			result.add(String.format("   %s %s", this.adaptabilityAmountPrefix.getFormattedText(), this.getNumberFormatter().format(cap.getAdaptiveAmount())));
		}
		return Optional.of(result);
		
	}

	private String makeOneMobResistString(float amount, DDDDamageType type) {
		boolean isNegative = amount < 0;
		return String.format("   %s%s %s %s", isNegative ? TextFormatting.DARK_RED.toString() : TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), isNegative ? this.weaknessSuffix.getFormattedText() : this.resistanceSuffix.getFormattedText());
	}
	
	private String makeOneMobResistString(float resist, ArmorValues armor, DDDDamageType type) {
		boolean isNegative = resist < 0;
		return String.format("   %s%s %s %s (%s)", isNegative ? TextFormatting.DARK_RED.toString() : TextFormatting.GRAY.toString(), this.getNumberFormatter().format(resist), this.getDamageFormatter().format(type), isNegative ? this.weaknessSuffix.getFormattedText() : this.resistanceSuffix.getFormattedText(), new TextComponentTranslation("tooltips.distinctdamagedescriptions.hwyla.witharmor", String.valueOf(this.getNumberFormatter().format(1-(1-resist)*(1 - armor.getArmor()/100.0f)))));
	}
	
	private Optional<String> makeImmunityString(Set<DDDDamageType> immunities) {
		if(immunities.isEmpty()) {
			return Optional.empty();
		}
		String str = this.immunityPrefix.getFormattedText() + " ";
		String[] strings = new String[immunities.size()];
		strings = immunities.stream().sorted().map(DDDDamageFormatter.STANDARD::format).collect(Collectors.toList()).toArray(strings);
		return Optional.of(str + YLib.joinNiceString(true, ",", strings));
	}
}
