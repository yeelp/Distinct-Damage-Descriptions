package yeelp.distinctdamagedescriptions.integration.capability.impl;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

public abstract class DistributionRequiresUpdate<D extends IDistribution> implements IDistributionRequiresUpdate {

	protected DDDBaseMap<Float> baseDist;

	@Override
	public final Optional<Map<DDDDamageType, Float>> getUpdatedDistribution(ItemStack stack) {
		if(this.baseDist == null || this.baseDist.isEmpty()) {
			D base = this.getDistributionCapabilityOnStack(stack);
			this.baseDist = base.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(0.0f, base::getWeight));
		}
		return this.calculateNewMap(stack);
	}

	@Override
	public final void applyDistributionToStack(ItemStack stack) {
		this.getUpdatedDistribution(stack).ifPresent(this.getDistributionCapabilityOnStack(stack)::setNewWeights);
	}

	@Override
	public NBTTagList serializeNBT() {
		return DDDBaseMap.toNBT(this.baseDist == null ? Maps.newHashMap() : this.baseDist);
	}

	@Override
	public void deserializeNBT(NBTTagList nbt) {
		this.baseDist = DDDBaseMap.fromNBT(nbt, 0.0f);
	}

	protected abstract D getDistributionCapabilityOnStack(ItemStack stack);
	
	protected abstract Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack);
}
