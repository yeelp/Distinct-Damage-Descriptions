package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.providers.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public class DamageDistribution extends Distribution implements IDamageDistribution {

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
		return capability == DamageDistributionProvider.damageDist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == DamageDistributionProvider.damageDist ? DamageDistributionProvider.damageDist.<T>cast(this) : null;
	}

	@Override
	public DamageMap distributeDamage(float dmg) {
		if(ModConfig.dmg.useCustomDamageTypes || this.distMap.keySet().stream().filter((k) -> k.isCustomDamage()).count() == 0) {
			return super.distribute(new DamageMap(), (f) -> f * dmg);
		}
		Stream<Entry<DDDDamageType, Float>> stream = this.distMap.entrySet().stream();
		long regularTypes = stream.filter((e) -> !e.getKey().isCustomDamage()).count();
		if(regularTypes == 0) {
			return DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution().distributeDamage(dmg);
		}
		DamageMap map = new DamageMap();
		float lostWeight = stream.map((e) -> e.getKey().isCustomDamage() ? e.getValue() : 0.0f).reduce(0.0f, (u, v) -> u + v, (u, v) -> u + v);
		float weightToAdd = lostWeight / regularTypes;
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			if(!entry.getKey().isCustomDamage()) {
				map.put(entry.getKey(), (entry.getValue() + weightToAdd) * dmg);
			}
		}
		return map;
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IDamageDistribution.class, new DamageDistributionStorage(), new DamageDistributionFactory());
	}

	private static class DamageDistributionFactory implements Callable<IDamageDistribution> {
		public DamageDistributionFactory() {
		}

		@Override
		public IDamageDistribution call() throws Exception {
			return new DamageDistribution();
		}
	}

	private static class DamageDistributionStorage implements IStorage<IDamageDistribution> {

		public DamageDistributionStorage() {
		}

		@Override
		public NBTBase writeNBT(Capability<IDamageDistribution> capability, IDamageDistribution instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IDamageDistribution> capability, IDamageDistribution instance, EnumFacing side, NBTBase nbt) {
			instance.deserializeNBT((NBTTagList) nbt);
		}
	}

	@Override
	public IDistribution copy() {
		return new DamageDistribution(super.copyMap(0));
	}
}
