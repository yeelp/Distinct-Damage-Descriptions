package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatItemDamageTooltipFormatterWrapper;

public final class SpellcastingItemDamageDistributionFormatter extends AbstractModCompatItemDamageTooltipFormatterWrapper {

	private static SpellcastingItemDamageDistributionFormatter instance;

	private SpellcastingItemDamageDistributionFormatter() {
		//nothing
	}

	@Override
	public boolean checkForSpecificApplicability(ItemStack t) {
		return SpellDistributionItemFormatter.getInstance().applicable(t);
	}

	public static SpellcastingItemDamageDistributionFormatter getInstance() {
		return instance == null ? new SpellcastingItemDamageDistributionFormatter() : instance;
	}
}
