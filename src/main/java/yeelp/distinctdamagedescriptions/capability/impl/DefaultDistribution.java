package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public abstract class DefaultDistribution extends Distribution implements IDistribution {

	@SafeVarargs
	protected DefaultDistribution(Tuple<DDDDamageType, Float>... weights) {
		super(weights);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}

	@Override
	public IDistribution copy() {
		return this;
	}

	@Override
	public IDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	public NBTTagList serializeNBT() {
		throw new UnsupportedOperationException("Default distributions have no NBT!");
	}

	@Override
	public void deserializeNBT(NBTTagList lst) {
		throw new UnsupportedOperationException("Default distributions have no NBT!");
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("Default distributions can't update weights!");
	}

	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("Default distributions can't update weights!");
	}
}
