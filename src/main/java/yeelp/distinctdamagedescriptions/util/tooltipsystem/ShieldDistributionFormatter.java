package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

/**
 * The singleton instance for formatting {@link ShieldDistribution} instances
 * @author Yeelp
 *
 */
public class ShieldDistributionFormatter extends AbstractCapabilityTooltipFormatter<ShieldDistribution, ItemStack> {
	
	private static ShieldDistributionFormatter instance;
	
	protected ShieldDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getShieldDistribution, "shielddist");
	}
	
	/**
	 * Return the singleton formatter instance if it exists, creating a new instance if it doesn't.
	 * @return The singleton instance, or a new instance if it doesn't exist yet.
	 */
	public static ShieldDistributionFormatter getInstance() {
		return instance == null ? instance = new ShieldDistributionFormatter() : instance;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f != DDDNumberFormatter.PLAIN;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, ShieldDistribution cap) {
		if(cap == null) {
			return Optional.empty();
		}
		return Optional.of(cap.getCategories().stream().sorted().collect(LinkedList<String>::new, (l, d) -> l.add(TooltipTypeFormatter.SHIELD.format(d, cap.getWeight(d), this)), LinkedList<String>::addAll));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.SHIELD;
	}

}
