package yeelp.distinctdamagedescriptions.integration.baubles.client;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatItemDamageTooltipFormatterWrapper;

public final class BaublesItemDamageDistributionFormatter extends AbstractModCompatItemDamageTooltipFormatterWrapper {

	private static BaublesItemDamageDistributionFormatter instance;
	
	private BaublesItemDamageDistributionFormatter() {
		//nothing
	}	
	
	@Override
	protected boolean checkForSpecificApplicability(ItemStack t) {
		return t.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
	}
	
	public static BaublesItemDamageDistributionFormatter getInstance() {
		return instance == null ? new BaublesItemDamageDistributionFormatter() : instance;
	}
}
