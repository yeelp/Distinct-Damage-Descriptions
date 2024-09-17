package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ArmorDistribution extends Distribution implements IArmorDistribution {

	@CapabilityInject(IArmorDistribution.class)
	public static Capability<IArmorDistribution> cap;

	public ArmorDistribution() {
		this(new NonNullMap<DDDDamageType, Float>(() -> 0.0f));
	}

	@SafeVarargs
	public ArmorDistribution(Tuple<DDDDamageType, Float>... weights) {
		super(weights);
		for(Tuple<DDDDamageType, Float> t : weights) {
			if(t.getSecond() == 0.0f) {
				this.distMap.put(t.getFirst(), t.getSecond());
			}
		}
	}

	public ArmorDistribution(Map<DDDDamageType, Float> resistMap) {
		super(resistMap);
		resistMap.forEach((type, weight) -> {
			if(weight == 0.0f) {
				this.distMap.put(type, weight);
			}
		});
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
		return super.distribute(DDDMaps.newArmorMap(), (f) -> new ArmorValues(armor * f, toughness * f));
	}

	@Override
	public IArmorDistribution copy() {
		return new ArmorDistribution(super.copyMap(0));
	}

	@Override
	public IArmorDistribution update(ItemStack owner) {
		return this;
	}
	
	@Override
	protected boolean allowZeroWeightedEntries() {
		return true;
	}
}
