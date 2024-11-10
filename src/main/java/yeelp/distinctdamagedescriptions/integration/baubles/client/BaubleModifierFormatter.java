package yeelp.distinctdamagedescriptions.integration.baubles.client;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import baubles.api.IBauble;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.baubles.BaublesConfigurations;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;
import yeelp.distinctdamagedescriptions.util.Translations.LayeredTranslator;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractKeyTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;

public final class BaubleModifierFormatter extends AbstractKeyTooltipFormatter<ItemStack> implements IModCompatTooltipFormatter<ItemStack> {

	private static BaubleModifierFormatter instance;
	
	static final Comparator<Entry<DDDDamageType, Float>> POSITIVES_FIRST = (e1, e2) -> {
		if(e1.getValue() * e2.getValue() < 0) {
			return e1.getValue() > 0 ? -1 : 1;
		}
		return e1.getKey().compareTo(e2.getKey());
	};
	
	private static final LayeredTranslator TRANSLATOR = Translations.INSTANCE.getLayeredTranslator(TooltipConsts.TOOLTIPS_ROOT, IntegrationIds.BAUBLES_ID);
	private static final BasicTranslator BASE_TRANSLATOR = Translations.INSTANCE.getTranslator(TooltipConsts.TOOLTIPS_ROOT);
	private static final Style GRAY = new Style().setColor(TextFormatting.GRAY);
	
	private static final ITextComponent BRUTE_FORCE_TEXT = TRANSLATOR.getComponent("bruteforce", GRAY);
	private static final ITextComponent SLY_STRIKE_TEXT = TRANSLATOR.getComponent("slystrike", GRAY);
	private static final ITextComponent RESISTANCE_TEXT = BASE_TRANSLATOR.getComponent(TooltipTypeFormatter.RESISTANCE, GRAY);
	private static final ITextComponent DAMAGE_TEXT = BASE_TRANSLATOR.getComponent(TooltipTypeFormatter.DAMAGE, GRAY);
	private static final ITextComponent IMMUNITY_TEXT = TRANSLATOR.getComponent("immunity", new Style().setColor(TextFormatting.AQUA));
	static final ITextComponent START = TRANSLATOR.getComponent("modifiers", GRAY);

	private BaubleModifierFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED);
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	public List<String> format(ItemStack t) {
		List<String> result = Lists.newArrayList();
		result.add(START.getFormattedText() + this.getKeyText());
		if(!this.shouldShow()) {
			return result;
		}
		//@formatter:off
		YResources.getRegistryString(t).filter(BaublesConfigurations.baubleModifiers::configured)
		.map(BaublesConfigurations.baubleModifiers::get)
		.ifPresent((m) -> {
			for(BaubleModifierType type : BaubleModifierType.values()) {
				Comparator<Entry<DDDDamageType, Float>> comparator = getComparator(type);
				m.get(type).getModifications().entrySet().stream()
					.filter((e) -> !e.getKey().isHidden())
					.sorted(comparator).map((e) -> this.formatEntry(type, e.getKey(), e.getValue())).forEach(result::add);
			}
		});
		return result;
		//@formatter:on
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_RESISTANCES;
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof IBauble && YResources.getRegistryString(t).filter(BaublesConfigurations.baubleModifiers::configured).isPresent();
	}

	@Override
	public IconAggregator getIconAggregator() {
		return BaubleModifierIconAggregator.getInstance();
	}

	@SuppressWarnings("incomplete-switch")
	private String formatEntry(BaubleModifierType baubleType, DDDDamageType type, float weight) {
		switch(baubleType) {
			case BRUTE_FORCE:
				return String.format("   %s %s%s %s %s", BRUTE_FORCE_TEXT.getFormattedText(), TextFormatting.GRAY.toString(), this.getNumberFormattingStrategy().format(weight), this.getDamageFormatter().format(type), RESISTANCE_TEXT.getFormattedText());
			case DAMAGE_MOD:
				return this.formatDamageOrResistance(baubleType, type, weight);
			case IMMUNITY:
				return this.formatImmunityOrSlyStrike(baubleType, type, weight);
			case RESISTANCE_MOD:
				return this.formatDamageOrResistance(baubleType, type, weight);
			case SLY_STRIKE:
				return this.formatImmunityOrSlyStrike(baubleType, type, weight);
		}
		DistinctDamageDescriptions.warn("Invalid Bauble Modifier Type found! " + baubleType.toString());
		return "";
	}
	
	static Comparator<Entry<DDDDamageType, Float>> getComparator(BaubleModifierType type) {
		return type.allowsNegative() ? POSITIVES_FIRST : Comparator.comparing(Entry<DDDDamageType, Float>::getKey).thenComparing(Entry::getValue);
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}
	
	public static BaubleModifierFormatter getInstance() {
		return instance == null ? new BaubleModifierFormatter() : instance;
	}
	
	private String formatDamageOrResistance(BaubleModifierType baubleType, DDDDamageType type, float weight) {
		String suffix = (baubleType == BaubleModifierType.DAMAGE_MOD ? DAMAGE_TEXT : RESISTANCE_TEXT).getFormattedText();
		return String.format("   %s%s %s %s", baubleType.getColourScheme().getFormattingBasedOnValue(weight, 0), DDDNumberFormatter.RELATIVE_TO_ZERO.format(weight), this.getDamageFormatter().format(type), suffix);
	}
	
	private String formatImmunityOrSlyStrike(BaubleModifierType baubleType, DDDDamageType type, float weight) {
		String chance =  weight < 1.0f ? TRANSLATOR.translate("immunitychance", GRAY, this.getNumberFormattingStrategy().format(weight)) : "";
		String damageTypeText = this.getDamageFormatter().format(type);
		String text1, text2;
		if(baubleType == BaubleModifierType.IMMUNITY) {
			text1 = damageTypeText;
			text2 = IMMUNITY_TEXT.getFormattedText();
		}
		else {
			text1 = SLY_STRIKE_TEXT.getFormattedText();
			text2 = damageTypeText;
		}
		return "   " + String.format("%s %s %s%s", text1, text2, TextFormatting.GRAY.toString(), chance).trim();
	}

}
