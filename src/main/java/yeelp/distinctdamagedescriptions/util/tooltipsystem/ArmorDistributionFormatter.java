package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;

/**
 * The singleton ArmorDistributionFormatter instance for formatting
 * {@link IArmorDistribution} instances.
 * 
 * @author Yeelp
 *
 */
public class ArmorDistributionFormatter extends AbstractCapabilityTooltipFormatter<IArmorDistribution> {

	private static ArmorDistributionFormatter instance;
	private final ITextComponent armorEffectivenessSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveness").setStyle(new Style().setColor(TextFormatting.GRAY));
	private final ITextComponent effectiveArmorSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveArmor").setStyle(new Style().setColor(TextFormatting.GRAY));
	private final ITextComponent effectiveToughnessSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveToughness").setStyle(new Style().setColor(TextFormatting.GRAY));
	
	private ArmorDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getArmorResistances, new TextComponentTranslation("tooltips.distinctdamagedescriptions.armorresistances").setStyle(new Style().setColor(TextFormatting.GRAY)));
	}

	/**
	 * Get the singleton ArmorDistributionFormatter instance, creating a new
	 * instance if it doesn't exist yet.
	 * 
	 * @return The singleton instance, or a new instance if it doesn't exist yet.
	 */
	public static ArmorDistributionFormatter getInstance() {
		return instance == null ? instance = new ArmorDistributionFormatter() : instance;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return true;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, IArmorDistribution cap) {
		if(cap == null) {
			return Optional.empty();
		}
		switch(this.getNumberFormatter()) {
			case PLAIN:
				ItemArmor armor = (ItemArmor) stack.getItem();
				ArmorMap aMap = cap.distributeArmor(armor.damageReduceAmount, armor.toughness);
				return Optional.of(aMap.entrySet().stream().sorted(Comparator.comparing(Entry<DDDDamageType, ArmorValues>::getKey).thenComparing(Entry::getValue)).collect(LinkedList<String>::new, (l, e) -> l.add(makeOneArmorEffectivenessString(e.getValue().getArmor(), e.getValue().getToughness(), e.getKey())), LinkedList<String>::addAll));
			case PERCENT:
			default:
				return Optional.of(cap.getCategories().stream().sorted().collect(LinkedList<String>::new, (l, d) -> l.add(makeOneArmorEffectivenessString(cap.getWeight(d), d)), LinkedList<String>::addAll));
		}
	}

	private String makeOneArmorEffectivenessString(float amount, DDDDamageType type) {
		return String.format("   %s%s %s %s", TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), this.armorEffectivenessSuffix.getFormattedText());
	}

	private String makeOneArmorEffectivenessString(float armor, float toughness, DDDDamageType type) {
		String gray = TextFormatting.GRAY.toString();
		return String.format("   %s%s: (%s %s%s, %s %s%s)%s", this.getDamageFormatter().format(type), gray, this.getNumberFormatter().format(armor), this.effectiveArmorSuffix.getFormattedText(), gray, this.getNumberFormatter().format(toughness), this.effectiveToughnessSuffix.getFormattedText(), gray, TextFormatting.RESET.toString());
	}
}