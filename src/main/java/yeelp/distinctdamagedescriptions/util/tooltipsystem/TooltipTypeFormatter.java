package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

/**
 * The class that formats individual tooltip string entries
 * 
 * @author Yeelp
 *
 */
public abstract class TooltipTypeFormatter {

	public static final String DAMAGE = "damage";
	public static final String RESISTANCE = "resistance";
	public static final String WEAKNESS = "weakness";
	public static final String EFFECTIVENESS = "effectiveness";
	public static final String ADAPTABILITY_CHANCE = "adaptivechance";
	public static final String ADAPTABILITY_AMOUNT = "adaptiveamount";
	public static final String IMMUNITIES = "immunities";

	protected static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(TooltipConsts.TOOLTIPS_ROOT);
	protected static final Style GRAY = new Style().setColor(TextFormatting.GRAY);

	/**
	 * Default formatter for damage
	 */
	public static final TooltipTypeFormatter DEFAULT_DAMAGE = new Default(DAMAGE);

	/**
	 * Formatter for armor.
	 */
	public static final Armor ARMOR = new Armor();

	/**
	 * Formatter for shields' effectiveness
	 */
	public static final TooltipTypeFormatter SHIELD = new Default(EFFECTIVENESS);

	/**
	 * Formatter for mob resistances
	 */
	public static final MobResistances MOB_RESISTS = new MobResistances();

	protected ITextComponent suffix;

	protected TooltipTypeFormatter(String key) {
		this.suffix = TRANSLATOR.getComponent(key, GRAY);
	}

	protected final String standardFormat(TextFormatting colorStart, DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
		return String.format("   %s%s %s %s", colorStart.toString(), formatter.getNumberFormattingStrategy().format(amount), formatter.getDamageFormatter().format(type), this.suffix.getFormattedText());
	}

	protected final String defaultFormat(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
		return this.standardFormat(TextFormatting.GRAY, type, amount, formatter);
	}

	/**
	 * Format a damage type value pair
	 * 
	 * @param type      the type of damage
	 * @param amount    the amount of that damage
	 * @param formatter the formatter whose number and damage formatter are to be
	 *                  used.
	 * @return the formatted string.
	 */
	public abstract String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter);

	/**
	 * Basic implementation
	 * 
	 * @author Yeelp
	 *
	 */
	public static final class Default extends TooltipTypeFormatter {

		Default(String key) {
			super(key);
		}

		@Override
		public String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
			return super.defaultFormat(type, amount, formatter);
		}
	}

	/**
	 * Implementation for armor
	 * 
	 * @author Yeelp
	 *
	 */
	public static final class Armor extends TooltipTypeFormatter {

		private static final ITextComponent ARMOR = TRANSLATOR.getComponent("effectiveArmor", GRAY);
		private static final ITextComponent TOUGHNESS = TRANSLATOR.getComponent("effectiveToughness", GRAY);

		Armor() {
			super(EFFECTIVENESS);
		}

		@Override
		public String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
			return super.standardFormat(formatter.getColourScheme().getFormattingBasedOnValue(amount, 1.0f), type, amount, formatter);
		}

		/**
		 * Format armor values as a damage type, armor, toughness triplet
		 * 
		 * @param type      the damage type
		 * @param armor     the armor value
		 * @param toughness the toughness value
		 * @param formatter the formatter whose number and damage formatters are to be
		 *                  used
		 * @return the formatted String.
		 */
		@SuppressWarnings("static-method")
		public final String formatArmorAndToughness(DDDDamageType type, float armor, float toughness, AbstractTooltipFormatter<?> formatter) {
			TextFormatting gray = TextFormatting.GRAY;
			return String.format("   %s%s: (%s %s%s, %s %s%s)%s", formatter.getDamageFormatter().format(type), gray.toString(), formatter.getNumberFormattingStrategy().format(armor), ARMOR.getFormattedText(), gray.toString(), formatter.getNumberFormattingStrategy().format(toughness), TOUGHNESS.getFormattedText(), gray.toString(), TextFormatting.RESET.toString());
		}
	}

	/**
	 * Implementation for Mob Resistances
	 * 
	 * @author Yeelp
	 *
	 */
	public static final class MobResistances extends TooltipTypeFormatter {

		private static final Style LIGHT_PURPLE = new Style().setColor(TextFormatting.LIGHT_PURPLE);
		private static final ITextComponent RESISTANCE_SUFFIX = TRANSLATOR.getComponent(RESISTANCE, GRAY);
		private static final ITextComponent WEAKNESS_SUFFIX = TRANSLATOR.getComponent(WEAKNESS, new Style().setColor(TextFormatting.DARK_RED));
		private static final ITextComponent ADAPTABILITY_CHANCE_PREFIX = TRANSLATOR.getComponent(ADAPTABILITY_CHANCE, LIGHT_PURPLE);
		private static final ITextComponent ADAPTABILITY_AMOUNT_PREFIX = TRANSLATOR.getComponent(ADAPTABILITY_AMOUNT, LIGHT_PURPLE);
		private static final ITextComponent STARTING_IMMUNITIES = TRANSLATOR.getComponent(IMMUNITIES, new Style().setColor(TextFormatting.AQUA));

		MobResistances() {
			super(RESISTANCE);
		}

		@Override
		public String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
			if(amount < 0) {
				this.suffix = WEAKNESS_SUFFIX;
			}
			else {
				this.suffix = RESISTANCE_SUFFIX;
			}
			return super.standardFormat(formatter.getColourScheme().getFormattingBasedOnValue(amount, 0.0f), type, amount, formatter);
		}

		/**
		 * Format mob adaptability
		 * 
		 * @param chance the adaptive chance
		 * @param amount the adpative amount
		 * @return two formatted strings if {@code chance != 0 || amount != 0} otherwise
		 *         an empty list.
		 */
		@SuppressWarnings("static-method")
		public final List<String> formatAdaptability(float chance, float amount) {
			if(chance == 0 && amount == 0) {
				return Collections.emptyList();
			}
			String str1 = String.format("%s %s", ADAPTABILITY_CHANCE_PREFIX.getFormattedText(), DDDNumberFormatter.PERCENT.format(chance));
			String str2 = String.format("   %s %s", ADAPTABILITY_AMOUNT_PREFIX.getFormattedText(), DDDNumberFormatter.PERCENT.format(amount));
			return ImmutableList.of(str1, str2);
		}

		/**
		 * Format immunities
		 * 
		 * @param immunities set of damage types with immunity
		 * @return an Optional containing the formatted string if the set is non empty,
		 *         otherwise, and empty Optional.
		 */
		@SuppressWarnings("static-method")
		public final Optional<String> formatImmunities(Set<DDDDamageType> immunities) {
			if(immunities.isEmpty()) {
				return Optional.empty();
			}
			String str = STARTING_IMMUNITIES.getFormattedText() + " ";
			String[] strings = new String[immunities.size()];
			strings = immunities.stream().sorted().map(DDDDamageFormatter.COLOURED::format).collect(Collectors.toList()).toArray(strings);
			return Optional.of(str + YLib.joinNiceString(true, ",", strings));
		}
	}
}
