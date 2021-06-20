package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.ShieldDistribution;

/**
 * The singleton instance for formatting {@link ShieldDistribution} instances
 * @author Yeelp
 *
 */
public class ShieldDistributionFormatter extends AbstractCapabilityTooltipFormatter<ShieldDistribution> {
	
	private final ITextComponent shieldEffectivenessSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.shielddist").setStyle(new Style().setColor(TextFormatting.GRAY));
	private static ShieldDistributionFormatter instance;
	
	protected ShieldDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getShieldDistribution, new TextComponentTranslation("tooltips.distinctdamagedescriptions.effectiveness").setStyle(new Style().setColor(TextFormatting.GRAY)));
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
		return Optional.of(cap.getCategories().stream().sorted().collect(LinkedList<String>::new, (l, d) -> l.add(makeOneShieldDistString(cap.getWeight(d), d)), LinkedList<String>::addAll));
	}
	
	private String makeOneShieldDistString(float amount, DDDDamageType type) {
		return String.format("   %s%s %s %s", TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), this.shieldEffectivenessSuffix.getFormattedText());
	}

}
