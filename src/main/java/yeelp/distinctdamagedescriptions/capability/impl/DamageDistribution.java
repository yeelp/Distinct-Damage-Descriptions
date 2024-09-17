package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public class DamageDistribution extends Distribution implements IDamageDistribution {

	@CapabilityInject(IDamageDistribution.class)
	public static Capability<IDamageDistribution> cap;

	protected static boolean invariantViolated(Collection<Float> weights) {
		float sum = 0.0f;
		for(float f : weights) {
			sum += f;
		}
		return !(Math.abs(sum - 1) <= 0.01) || Distribution.invariantViolated(weights);
	}

	public DamageDistribution() {
		this(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, 1.0f));
	}

	@SafeVarargs
	public DamageDistribution(Tuple<DDDDamageType, Float>... weights) {
		super(weights);
		if(invariantViolated(this.distMap.values())) {
			throw new InvariantViolationException("weights are negative or do not add to 1!");
		}
	}

	public DamageDistribution(Map<DDDDamageType, Float> weightMap) {
		super(weightMap);
		if(invariantViolated(this.distMap.values())) {
			throw new InvariantViolationException("weights are negative or do not add to 1!");
		}
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
	public DamageMap distributeDamage(float dmg) {
		if(this.distMap.keySet().stream().filter((k) -> !k.isUsable()).count() == 0) {
			return super.distribute(DDDMaps.newDamageMap(), (f) -> f * dmg);
		}
		long regularTypes = this.distMap.entrySet().stream().filter((e) -> e.getKey().isUsable()).count();
		if(regularTypes == 0) {
			return DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution().distributeDamage(dmg);
		}
		DamageMap map = DDDMaps.newDamageMap();
		float lostWeight = (float) this.distMap.entrySet().stream().mapToDouble((e) -> !e.getKey().isUsable() ? e.getValue() : 0.0f).sum();
		float weightToAdd = lostWeight / regularTypes;
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			if(entry.getKey().isUsable()) {
				map.put(entry.getKey(), (entry.getValue() + weightToAdd) * dmg);
			}
		}
		return map;
	}

	@Override
	public IDamageDistribution copy() {
		return new DamageDistribution(super.copyMap(0));
	}

	@Override
	public IDamageDistribution update(ItemStack owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(EntityLivingBase owner) {
		return this;
	}

	@Override
	public IDamageDistribution update(IProjectile owner) {
		return this;
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		throw new InvariantViolationException("Can't set individual damage types weight as weights would no longer add to 1! Use setNewWeights() instead!");
	}
	
	@Override
	protected boolean allowZeroWeightedEntries() {
		return false;
	}

}
