package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;

/**
 * A capability formatter specifically for {@link IDamageDistribution}
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractDamageDistributionFormatter extends AbstractCapabilityTooltipFormatter<IDamageDistribution, ItemStack> {

	protected AbstractDamageDistributionFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<ItemStack, Optional<IDamageDistribution>> capExtractor, String typeTextKey) {
		super(keyTooltip, numberFormatter, damageFormatter, capExtractor, typeTextKey);
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, IDamageDistribution cap) {
		if(!this.shouldShowDist(stack)) {
			return Optional.empty();
		}
		final DamageMap vals = this.getVals(stack, cap);
		List<String> lst = vals.entrySet().stream().sorted(Comparator.comparing(Entry<DDDDamageType, Float>::getKey).thenComparing(Entry::getValue)).collect(LinkedList<String>::new, (l, d) -> l.add(TooltipTypeFormatter.DEFAULT_DAMAGE.format(d.getKey(), vals.get(d.getKey()), this)), LinkedList<String>::addAll);
		return Optional.of(lst);
	}

	/**
	 * Should this distribution be shown? This differs from
	 * {@link AbstractKeyTooltipFormatter#shouldShow()} in that this determines if
	 * there is content to show and if said content should be shown, regardless if
	 * the conditions to show that content is met are met.
	 * 
	 * @param stack
	 * @return
	 */
	protected abstract boolean shouldShowDist(ItemStack stack);

	/**
	 * Get the amount of damage to distribute for this ItemStack
	 * 
	 * @param stack
	 * @return The amount of damage this ItemStack does
	 */
	protected abstract float getDamageToDistribute(ItemStack stack);

	/**
	 * Get number values for this capability, depending on the current
	 * {@link DDDNumberFormatter}, if applicable. The default implementation returns
	 * {@code cap.distributeDamage(1)}, which effectively is a Map that maps damage
	 * types to the passed capability's weights.
	 * 
	 * @param stack the stack context
	 * @param cap   the capability instance
	 * @return an Map mapping types to weights or the distributed damage values.
	 */
	protected DamageMap getVals(ItemStack stack, IDamageDistribution cap) {
		return cap.distributeDamage(this.getDamageToDistribute(stack));
	}
}
