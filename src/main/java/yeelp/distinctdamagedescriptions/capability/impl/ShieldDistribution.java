package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ShieldDistribution extends Distribution implements IDistribution {
	
	@CapabilityInject(ShieldDistribution.class)
	public static Capability<ShieldDistribution> cap;
	
	public ShieldDistribution() {
		this(new NonNullMap<DDDDamageType, Float>(1.0f));
	}

	public ShieldDistribution(Map<DDDDamageType, Float> blockMap) {
		super(blockMap);
	}

	@SafeVarargs
	public ShieldDistribution(Tuple<DDDDamageType, Float>... mappings) {
		super(mappings);
	}

	public DamageMap block(DamageMap fullDamage) {
		DamageMap remainingDamage = new DamageMap();
		for(Entry<DDDDamageType, Float> entry : fullDamage.entrySet()) {
			remainingDamage.put(entry.getKey(), blockDamage(entry.getValue(), this.getWeight(entry.getKey())));
		}
		return remainingDamage;
	}

	public static void register() {
		DDDCapabilityBase.register(ShieldDistribution.class, NBTTagList.class, ShieldDistribution::new);
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
	public IDistribution copy() {
		return new ShieldDistribution(super.copyMap(1.0f));
	}
	
	private static float blockDamage(float damage, float weight) {
		return MathHelper.clamp(damage * (1 - weight), 0, Float.MAX_VALUE);
	}
}