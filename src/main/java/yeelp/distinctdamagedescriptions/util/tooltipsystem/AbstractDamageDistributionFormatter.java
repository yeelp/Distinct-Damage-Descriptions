package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

/**
 * A capability formatter specifically for {@link IDamageDistribution}
 * @author Yeelp
 *
 */
public abstract class AbstractDamageDistributionFormatter extends AbstractCapabilityTooltipFormatter<IDamageDistribution, ItemStack> {

	private final ITextComponent damageSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damage").setStyle(new Style().setColor(TextFormatting.GRAY));
	
	protected AbstractDamageDistributionFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<ItemStack, IDamageDistribution> capExtractor, ITextComponent typeText) {
		super(keyTooltip, numberFormatter, damageFormatter, capExtractor, typeText);
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack stack, IDamageDistribution cap) {
		if(cap == null || !this.shouldShowDist(stack)) {
			return Optional.empty();
		}
		List<String> lst = new LinkedList<String>();
		final Iterator<Float> vals = this.getVals(stack, cap);
		cap.getCategories().stream().sorted().forEach((d) -> lst.add(makeOneDamageString(vals.next(), d)));
		return Optional.of(lst);
	}
	
	/**
	 * Should this distribution be shown? This differs from {@link AbstractKeyTooltipFormatter#shouldShow()} 
	 * in that this determines if there is content to show and if said content should be shown, 
	 * regardless if the conditions to show that content is met are met.
	 * @param stack
	 * @return
	 */
	protected abstract boolean shouldShowDist(ItemStack stack);
	
	protected abstract float getDamageToDistribute(ItemStack stack);
	
	/**
	 * Get number values for this capability, depending on the current {@link DDDNumberFormatter}, if applicable. The default implementation
	 * returns {@code cap.distributeDamage(1).values().iterator()}, which effectively is an Iterator that iterates over the passed capability's weights.
	 * @param stack the stack context
	 * @param cap the capability instance
	 * @return an Iterator iterating over the weights or the distributed damage values.
	 */
	protected Iterator<Float> getVals(ItemStack stack, IDamageDistribution cap) {
		return cap.distributeDamage(this.getDamageToDistribute(stack)).values().iterator();
	}
	
	private String makeOneDamageString(float amount, DDDDamageType type) {
		return String.format("   %s%s %s %s", TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), this.damageSuffix.getFormattedText());
	}

}
