package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistributionRequiresUpdate;

public abstract class DistributionRequiresUpdate<D extends Distribution> implements IDistributionRequiresUpdate {

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
	public final Map<DDDDamageType, Float> getUpdatedDistribution(ItemStack stack) {
		if(this.map != null) {
			return this.map;
		}
		return this.map = this.calculateNewMap(stack);
	}

	@Override
	public final void applyDistributionToStack(ItemStack stack) {
		this.getDistributionCapabilityOnStack(stack).setNewWeights(this.getUpdatedDistribution(stack));
		this.updated = true;
	}

	@Override
	public NBTTagByte serializeNBT() {
		return new NBTTagByte(this.booleanByteValue(this.updated));
	}

	@Override
	public void deserializeNBT(NBTTagByte nbt) {
		this.updated = this.truthiness(nbt.getByte());
	}

	protected abstract D getDistributionCapabilityOnStack(ItemStack stack);
	
	protected abstract Map<DDDDamageType, Float> calculateNewMap(ItemStack stack);

	private boolean truthiness(byte b) {
		return b > 0;
	}
	
	private byte booleanByteValue(boolean b) {
		return (byte) (b ? 1 : 0);
	}
}
