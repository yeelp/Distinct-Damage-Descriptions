package yeelp.distinctdamagedescriptions.integration.hwyla.client;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;

/**
 * The singleton formatter for Hwyla tooltip formatting.
 * 
 * @author Yeelp
 * @param <C> The kind of content shown
 *
 */
public abstract class HwylaTooltipFormatter<C> extends AbstractCapabilityTooltipFormatter<C, EntityLivingBase> {

	private final KeyTooltip key;

	protected HwylaTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Function<EntityLivingBase, Optional<C>> capExtractor, String typeTextKey) {
		super(keyTooltip, numberFormatter, damageFormatter, capExtractor, typeTextKey);
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
