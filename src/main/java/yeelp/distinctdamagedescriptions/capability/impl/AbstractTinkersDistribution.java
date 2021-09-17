package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;

public abstract class AbstractTinkersDistribution<D extends IDistribution> extends DistributionRequiresUpdate<D> {
	
	protected abstract String getPartType();
	
	protected abstract Iterator<PartMaterialType> getParts();

	@Override
	protected Map<DDDDamageType, Float> calculateNewMap(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}
}
