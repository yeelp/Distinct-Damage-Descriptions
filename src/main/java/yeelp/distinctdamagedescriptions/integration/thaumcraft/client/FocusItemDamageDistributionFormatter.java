package yeelp.distinctdamagedescriptions.integration.thaumcraft.client;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatItemDamageTooltipFormatterWrapper;

public final class FocusItemDamageDistributionFormatter extends AbstractModCompatItemDamageTooltipFormatterWrapper {

	private static FocusItemDamageDistributionFormatter instance;

	private FocusItemDamageDistributionFormatter() {
		//nothing
	}
	
	@Override
	public boolean checkForSpecificApplicability(ItemStack t) {
		return FocusDistributionItemFormatter.getInstance().applicable(t);
	}
	
	public static FocusItemDamageDistributionFormatter getInstance() {
		return instance == null ? instance = new FocusItemDamageDistributionFormatter() : instance;
	}

}
