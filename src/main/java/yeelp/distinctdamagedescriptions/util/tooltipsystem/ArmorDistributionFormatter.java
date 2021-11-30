package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
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
public class ArmorDistributionFormatter extends AbstractCapabilityTooltipFormatter<IArmorDistribution, ItemStack> {

	private static ArmorDistributionFormatter instance;
	
	private ArmorDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getArmorResistances, "armorresistances");
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
		switch(this.getNumberFormatter()) {
			case PLAIN:
				ItemArmor armor = (ItemArmor) stack.getItem();
				ArmorMap aMap = cap.distributeArmor(armor.damageReduceAmount, armor.toughness);
				return Optional.of(aMap.entrySet().stream().sorted(Comparator.comparing(Entry<DDDDamageType, ArmorValues>::getKey).thenComparing(Entry::getValue)).collect(LinkedList<String>::new, (l, e) -> l.add(TooltipTypeFormatter.ARMOR.formatArmorAndToughness(e.getKey(), e.getValue().getArmor(), e.getValue().getToughness(), this)), LinkedList<String>::addAll));
			case PERCENT:
			default:
				return Optional.of(cap.getCategories().stream().sorted().collect(LinkedList<String>::new, (l, d) -> l.add(TooltipTypeFormatter.ARMOR.format(d, cap.getWeight(d), this)), LinkedList<String>::addAll));
		}
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.ARMOR;
	}
}