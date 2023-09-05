package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DefaultShieldDistribution extends ShieldDistribution {

	private static DefaultShieldDistribution instance;
	
	public static DefaultShieldDistribution getInstance() {
		return instance == null ? instance = new DefaultShieldDistribution() : instance;
	}
	
	private DefaultShieldDistribution() {
		super(new NonNullMap<DDDDamageType, Float>(() -> 0.0f));
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
	public ShieldDistribution copy() {
		return this;
	}

	@Override
	public ShieldDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	public NBTTagList serializeNBT() {
		throw new UnsupportedOperationException("Default Shield distributions have no NBT!");
	}

	@Override
	public void deserializeNBT(NBTTagList lst) {
		throw new UnsupportedOperationException("Default Shield distributions have no NBT!");
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		throw new UnsupportedOperationException("Can not set new weights in default shield distribution.");
	}

	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		throw new UnsupportedOperationException("Can not set new weights in default shield distribution.");
	}

}
