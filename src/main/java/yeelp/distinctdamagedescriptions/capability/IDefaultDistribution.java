package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public interface IDefaultDistribution extends IDistribution {

	@Override
	default NBTTagList serializeNBT() {
		throw new UnsupportedOperationException("Default distributions have no NBT!");
	}

	@Override
	default void deserializeNBT(NBTTagList nbt) {
		throw new UnsupportedOperationException("Default distributions have no NBT!");		
	}

	@Override
	default void setWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("Default distributions can't update weights!");		
	}

	@Override
	default void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("Default distributions can't update weights!");
	}

	@Override
	default IDistribution copy() {
		return this;
	}

	@Override
	default IDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	default boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}

	@Override
	default <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}
}
