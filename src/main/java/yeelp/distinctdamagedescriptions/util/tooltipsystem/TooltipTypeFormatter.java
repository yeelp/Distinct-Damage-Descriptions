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

	protected static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(TooltipConsts.TOOLTIPS_ROOT);
	protected static final Style GRAY = new Style().setColor(TextFormatting.GRAY);

	/**
	 * Default formatter for damage
	 */
	public static final TooltipTypeFormatter DEFAULT_DAMAGE = new Default(TooltipConsts.DAMAGE);

	/**
	 * Formatter for armor.
	 */
	public static final Armor ARMOR = new Armor.Vanilla();

	/**
	 * Formatter for shields' effectiveness
	 */
	public static final TooltipTypeFormatter SHIELD = new Default(TooltipConsts.EFFECTIVENESS);

	/**
	 * Formatter for mob resistances
	 */
	public static final MobResistances MOB_RESISTS = new MobResistances();

	protected ITextComponent suffix;

	protected TooltipTypeFormatter(String key) {
		this.suffix = TRANSLATOR.getComponent(key, GRAY);
	}

	protected final String standardFormat(TextFormatting colorStart, DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
		return regularFormat(colorStart, formatter.getDamageFormatter().format(type), amount, formatter);
	}
	
	protected final String regularFormat(TextFormatting colorStart, String typePlaceholder, float amount, AbstractTooltipFormatter<?> formatter) {
		return String.format("   %s%s %s %s", colorStart.toString(), formatter.getNumberFormattingStrategy().format(amount), typePlaceholder, this.suffix.getFormattedText());
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
	public static abstract class Armor extends TooltipTypeFormatter {

		protected Armor() {
			super(TooltipConsts.EFFECTIVENESS);
		}

		@Override
		public final String format(DDDDamageType type, float amount, AbstractTooltipFormatter<?> formatter) {
			return super.standardFormat(formatter.getColourScheme().getFormattingBasedOnValue(amount, 1.0f), type, amount, formatter);
		}
		
		public final String formatNoType(ITextComponent typePlaceholder, float amount, AbstractTooltipFormatter<?> formatter) {
			return super.regularFormat(formatter.getColourScheme().getFormattingBasedOnValue(amount, 1.0f), typePlaceholder.getFormattedText(), amount, formatter);
		}

		protected abstract ITextComponent getArmorText();

		protected abstract ITextComponent getToughnessText();

		/**
		 * Format armor values as a damage type, armor tuple, or as a triple if there is
		 * toughness
		 * 
		 * @param type              The damage type.
		 * @param armor             The armor values.
		 * @param toughness         The toughness value.
		 * @param originalArmor     The original armor value this armor piece provided.
		 *                          Used as a reference for colour coding.
		 * @param originalToughness The original toughess value this armor piece
		 *                          provided. If non-zero, toughness will be included in
		 *                          the tooltip, also used as a reference for colour
		 *                          coding.
		 * @param formatter         The formatter whose number and damage formatters are
		 *                          to be used.
		 * @return The formatted String.
		 */
		public final String formatArmorAndToughness(DDDDamageType type, float armor, float toughness, float originalArmor, float originalToughness, AbstractTooltipFormatter<?> formatter) {
			return formatArmorAndToughness(formatter.getDamageFormatter().format(type), armor, toughness, originalArmor, originalToughness, formatter);
		}
		
		/**
		 * Format armor values as a placeholder string, armor tuple, or as a triple if there is
		 * toughness
		 * 
		 * @param typePlaceholder   The string to place where a damage type would normally be
		 * @param armor             The armor values.
		 * @param toughness         The toughness value.
		 * @param originalArmor     The original armor value this armor piece provided.
		 *                          Used as a reference for colour coding.
		 * @param originalToughness The original toughess value this armor piece
		 *                          provided. If non-zero, toughness will be included in
		 *                          the tooltip, also used as a reference for colour
		 *                          coding.
		 * @param formatter         The formatter whose number formatter is
		 *                          to be used.
		 * @return The formatted String.
		 */
		public final String formatArmorAndToughness(String typePlaceholder, float armor, float toughness, float originalArmor, float originalToughness, AbstractTooltipFormatter<?> formatter) {
			TextFormatting gray = TextFormatting.GRAY;
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("   %s%s: (%s", typePlaceholder, gray.toString(), formatValue(armor, originalArmor, formatter, this.getArmorText().getFormattedText())));
			if(originalToughness != 0) {
				sb.append(String.format(", %s", formatValue(toughness, originalToughness, formatter, this.getToughnessText().getFormattedText())));
			}
			return sb.append(String.format("%s)", gray.toString())).toString();
		}

		private static String formatValue(float val, float threshold, AbstractTooltipFormatter<?> formatter, String suffix) {
			return String.format("%s%s%s %s", formatter.getColourScheme().getFormattingBasedOnValue(val, threshold), formatter.getNumberFormattingStrategy().format(val), TextFormatting.GRAY.toString(), suffix);
		}
		
		public static final class Vanilla extends Armor {
			
			private static final ITextComponent ARMOR = TRANSLATOR.getComponent("effectiveArmor", GRAY);
			private static final ITextComponent TOUGHNESS = TRANSLATOR.getComponent("effectiveToughness", GRAY);

			@Override
			protected ITextComponent getArmorText() {
				return ARMOR;
			}

			@Override
			protected ITextComponent getToughnessText() {
				return TOUGHNESS;
			}
			
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
		private static final ITextComponent RESISTANCE_SUFFIX = TRANSLATOR.getComponent(TooltipConsts.RESISTANCE, GRAY);
		private static final ITextComponent WEAKNESS_SUFFIX = TRANSLATOR.getComponent(TooltipConsts.WEAKNESS, new Style().setColor(TextFormatting.DARK_RED));
		private static final ITextComponent ADAPTABILITY_CHANCE_PREFIX = TRANSLATOR.getComponent(TooltipConsts.ADAPTABILITY_CHANCE, LIGHT_PURPLE);
		private static final ITextComponent ADAPTABILITY_AMOUNT_PREFIX = TRANSLATOR.getComponent(TooltipConsts.ADAPTABILITY_AMOUNT, LIGHT_PURPLE);
		private static final ITextComponent STARTING_IMMUNITIES = TRANSLATOR.getComponent(TooltipConsts.IMMUNITIES, new Style().setColor(TextFormatting.AQUA));

		MobResistances() {
			super(TooltipConsts.RESISTANCE);
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
