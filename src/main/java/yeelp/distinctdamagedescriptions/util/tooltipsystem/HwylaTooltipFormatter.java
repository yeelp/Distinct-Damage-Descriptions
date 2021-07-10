package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.function.Function;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;

/**
 * The singleton formatter for Hwyla tooltip formatting.
 * @author Yeelp
 * @param <C> The kind of content shown
 *
 */
public abstract class HwylaTooltipFormatter<C> extends AbstractCapabilityTooltipFormatter<C, EntityLivingBase> {
	
	private final KeyTooltip key;
	protected HwylaTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<EntityLivingBase, C> capExtractor, ITextComponent typeText) {
		super(keyTooltip, numberFormatter, damageFormatter, capExtractor, typeText);
		this.key = keyTooltip;
	}

	@Override
	public boolean shouldShow() {
		return this.key.checkKeyIsHeld();
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return f == DDDDamageFormatter.STANDARD;
	}

	@Override
	public String getKeyText() {
		return this.key.getKeyText();
	}
	
	
}
