package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public interface IDefaultDistribution extends IDistribution {

	@Override
	default NBTTagCompound serializeSpecificNBT() {
		throw new UnsupportedOperationException("Default distributions have no NBT!");
	}

	@Override
	default void deserializeSpecificNBT(NBTTagCompound nbt) {
		throw new UnsupportedOperationException("Default distributions have no NBT!");		
	}

	@Override
	default void setWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("Default distributions can't update weights!");		
	}
	
	@Override
	default void setBaseWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("Default distributions can't update weights!");		
	}

	@Override
	default void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("Default distributions can't update weights!");
	}
	
	@Override
	default void setNewBaseWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("Default distributions can't update weights!");
	}
	
	@Override
	default Set<DDDDamageType> getBaseCategories() {
		return this.getCategories();
	}
	
	@Override
	default float getBaseWeight(DDDDamageType type) {
		return this.getWeight(type);
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
	
	@Override
	default Set<String> getModifiers() {
		return Sets.newHashSet();
	}
	
	@Override
	default void addModifier(String s) {
		throw new UnsupportedOperationException("Default Distributions can't be modified!");
	}
	
	@Override
	default void removeModifier(String s) {
		throw new UnsupportedOperationException("Default Distributions can't be modified!");
	}
	
	@Override
	default Class<NBTTagCompound> getSpecificNBTClass() {
		return NBTTagCompound.class;
	}
}
