package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import com.google.common.base.Predicates;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The singleton instance for formatting {@link ShieldDistribution} instances
 * 
 * @author Yeelp
 *
 */
public class ShieldDistributionFormatter extends AbstractCapabilityTooltipFormatter<ShieldDistribution, ItemStack> {

	private static ShieldDistributionFormatter instance;

	protected ShieldDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getShieldDistribution, "shielddist");
	}

	/**
	 * Return the singleton formatter instance if it exists, creating a new instance
	 * if it doesn't.
	 * 
	 * @return The singleton instance, or a new instance if it doesn't exist yet.
	 */
	public static ShieldDistributionFormatter getInstance() {
		return instance == null ? instance = new ShieldDistributionFormatter() : instance;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(@Nonnull ItemStack stack, @Nonnull ShieldDistribution cap) {
		return Optional.of(cap.getCategories().stream().filter(Predicates.not(DDDDamageType::isHidden)).sorted().collect(LinkedList<String>::new, (l, d) -> l.add(TooltipTypeFormatter.SHIELD.format(d, cap.getWeight(d), this)), LinkedList<String>::addAll));
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.SHIELD;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}

}
