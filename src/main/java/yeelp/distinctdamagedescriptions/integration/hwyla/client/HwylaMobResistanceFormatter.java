package yeelp.distinctdamagedescriptions.integration.hwyla.client;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.ResistMap;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;

public class HwylaMobResistanceFormatter extends HwylaTooltipFormatter<IMobResistances> {

	private static HwylaMobResistanceFormatter instance;

	private static final Style WHITE = new Style().setColor(TextFormatting.WHITE), AQUA = new Style().setColor(TextFormatting.AQUA),
			LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE);

	private final ITextComponent noResists = AbstractCapabilityTooltipFormatter.getComponentWithStyle("noresists", WHITE),
			immunityPrefix = AbstractCapabilityTooltipFormatter.getComponentWithStyle("immunities", AQUA),
			adaptabilityAmountPrefix = AbstractCapabilityTooltipFormatter.getComponentWithStyle("adaptiveamount", LIGHT_PURPLE);

	private HwylaMobResistanceFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.STANDARD, DDDAPI.accessor::getMobResistances, "mobresistances");
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
		if(t == null || cap == null) {
			return Optional.empty();
		}
		List<String> result = new LinkedList<String>();
		Set<DDDDamageType> immunities = new HashSet<DDDDamageType>();
		ArmorMap aMap = new ArmorMap();
		t.getArmorInventoryList().forEach((stack) -> {
			if(stack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				IArmorDistribution armorDist = DDDAPI.accessor.getArmorResistances(stack);
				if(armorDist != null) {
					armorDist.distributeArmor(armor.damageReduceAmount, armor.toughness).forEach((type, armorValues) -> aMap.compute(type, (k, v) -> ArmorValues.merge(v, armorValues)));
				}
			}
		});
		ResistMap rMap = cap.getAllResistances();
		for(Iterator<DDDDamageType> it = DDDRegistries.damageTypes.getAll().stream().sorted(Comparator.<DDDDamageType>comparingDouble((d) -> rMap.get(d)).thenComparing(Comparator.naturalOrder())).iterator(); it.hasNext();) {
			DDDDamageType type = it.next();
			if(cap.hasImmunity(type)) {
				immunities.add(type);
			}
			String s = TooltipTypeFormatter.MOB_RESISTS.format(type, rMap.get(type), this);
			if(aMap.containsKey(type)) {
				result.add(s.concat(Translations.INSTANCE.getTranslator("tooltips").translate("hwyla.witharmor", new Style().setColor(TextFormatting.GRAY), String.valueOf(this.getNumberFormatter().format(1-(1-rMap.get(type))*(1 - aMap.get(type).getArmor()/100.0f))))));
			}
			else if (rMap.get(type) != 0) {
				result.add(s);
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
	
	private Optional<String> makeImmunityString(Set<DDDDamageType> immunities) {
		if(immunities.isEmpty()) {
			return Optional.empty();
		}
		String str = this.immunityPrefix.getFormattedText() + " ";
		String[] strings = new String[immunities.size()];
		strings = immunities.stream().sorted().map(DDDDamageFormatter.STANDARD::format).collect(Collectors.toList()).toArray(strings);
		return Optional.of(str + YLib.joinNiceString(true, ",", strings));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_RESISTANCES;
	}
}
