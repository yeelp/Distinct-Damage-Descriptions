package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ArmorDistribution extends Distribution implements IArmorDistribution {
	
	@CapabilityInject(IArmorDistribution.class)
	public static Capability<IArmorDistribution> cap;
	
	public ArmorDistribution() {
		this(new NonNullMap<DDDDamageType, Float>(0.0f));
	}

	@SafeVarargs
	public ArmorDistribution(Tuple<DDDDamageType, Float>... weights) {
		super(weights);
	}

	public ArmorDistribution(Map<DDDDamageType, Float> resistMap) {
		super(resistMap);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == cap ? cap.<T>cast(this) : null;
	}

	@Override
	public ArmorMap distributeArmor(float armor, float toughness) {
		return super.distribute(new ArmorMap(), (f) -> new ArmorValues(armor * f, toughness * f));
	}

	@Override
	public IDistribution copy() {
		return new ArmorDistribution(super.copyMap(0));
	}
}
