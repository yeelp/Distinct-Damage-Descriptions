package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ShieldDistribution extends Distribution implements IDistribution {

	@CapabilityInject(ShieldDistribution.class)
	public static Capability<ShieldDistribution> cap;

	public ShieldDistribution() {
		this(new NonNullMap<DDDDamageType, Float>(() -> 1.0f));
	}

	public ShieldDistribution(Map<DDDDamageType, Float> blockMap) {
		super(blockMap);
	}

	@SafeVarargs
	public ShieldDistribution(Tuple<DDDDamageType, Float>... mappings) {
		super(mappings);
	}

	public DamageMap block(DamageMap fullDamage) {
		this.getCategories().forEach((t) -> fullDamage.computeIfPresent(t, (k, v) -> {
			float f = blockDamage(v, this.getWeight(k));
			return f <= 0 ? null : f;
		}));
		return fullDamage;
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
	public ShieldDistribution copy() {
		return new ShieldDistribution(super.copyMap(1.0f));
	}

	@Override
	public ShieldDistribution update(ItemStack owner) {
		return this;
	}

	private static float blockDamage(float damage, float weight) {
		return MathHelper.clamp(damage * (1 - weight), 0, Float.MAX_VALUE);
	}
}