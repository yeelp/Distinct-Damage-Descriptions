package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class ShieldDistribution extends Distribution implements IDistribution {

	@CapabilityInject(ShieldDistribution.class)
	public static Capability<ShieldDistribution> cap;

	public ShieldDistribution() {
		this(new NonNullMap<DDDDamageType, Float>(() -> 0.0f));
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
		DebugLib.doDebug(() -> {
			DistinctDamageDescriptions.debug(String.format("After blocking: %s", DebugLib.entriesToString(fullDamage)));
		});
		return fullDamage;
	}

	public static void register() {
		DDDUpdatableCapabilityBase.register(ShieldDistribution.class, ShieldDistribution::new);
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
		return new ShieldDistribution(super.copyMap(0.0f));
	}

	@Override
	public ShieldDistribution update(ItemStack owner) {
		Set<String> mods = DDDRegistries.modifiers.getItemStackShieldDistributionRegistry().getNamesOfApplicableModifiers(owner);
		if(!YMath.setEquals(mods, this.getModifiers())) {
			this.distMap.clear();
			this.distMap.putAll(this.originalMap);
			DDDRegistries.modifiers.getItemStackShieldDistributionRegistry().applyModifiers(owner, this);
			this.updateModifiers(mods);
		}
		return this;
	}

	@Override
	protected boolean allowZeroWeightedEntries() {
		return false;
	}
	
	@Override
	protected boolean canHaveEmptyDistribution() {
		return true;
	}
	
	@Override
	public Class<NBTTagCompound> getSpecificNBTClass() {
		return NBTTagCompound.class;
	}
	
	private static float blockDamage(float damage, float weight) {
		return MathHelper.clamp(damage * (1 - weight), 0, Float.MAX_VALUE);
	}
}