package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistributionRequiresUpdate;

public abstract class DistributionRequiresUpdate<D extends IDistribution> implements IDistributionRequiresUpdate {

	private boolean updated = false;
	private Map<DDDDamageType, Float> map;
	@Override
	public final boolean updated() {
		return this.updated;
	}
	
	@Override
	public final void markForUpdate() {
		this.updated = false;
		this.map = null;
	}

	@Override
	public final Optional<Map<DDDDamageType, Float>> getUpdatedDistribution(ItemStack stack) {
		if(this.map != null) {
			return Optional.of(this.map);
		}
		Optional<Map<DDDDamageType, Float>> newMap = this.calculateNewMap(stack);
		if(newMap.isPresent()) {
			this.map = newMap.get(); 
		}
		return newMap;
	}

	@Override
	public final void applyDistributionToStack(ItemStack stack) {
		this.getUpdatedDistribution(stack).ifPresent(this.getDistributionCapabilityOnStack(stack)::setNewWeights);
		this.updated = true;
	}

	@Override
	public NBTTagByte serializeNBT() {
		return new NBTTagByte(booleanByteValue(this.updated));
	}

	@Override
	public void deserializeNBT(NBTTagByte nbt) {
		this.updated = truthiness(nbt.getByte());
	}

	protected abstract D getDistributionCapabilityOnStack(ItemStack stack);
	
	protected abstract Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack);

	private static boolean truthiness(byte b) {
		return b > 0;
	}
	
	private static byte booleanByteValue(boolean b) {
		return (byte) (b ? 1 : 0);
	}
}
