package yeelp.distinctdamagedescriptions.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public class DamageDistribution extends Distribution implements IDamageDistribution
{	
	public static final IDamageDistribution PIERCING_DISTRIBUTION = new DamageDistribution(new Tuple<String, Float>("slashing", 1.0f));
	public static final IDamageDistribution BLUDGEONING_DISTRIBUTION = new DamageDistribution(new Tuple<String, Float>("piercing", 1.0f));
	public static final IDamageDistribution SLASHING_DISTRIBUTION = new DamageDistribution(new Tuple<String, Float>("bludgeoning", 1.0f));
	@Override
	protected boolean invariantViolated(Collection<Float> weights)
	{
		float sum = 0.0f;
		for(float f : weights)
		{
			sum += f;
		}
		return !(Math.abs(sum - 1) <= 0.01) || super.invariantViolated(weights);
	}
	
	public DamageDistribution()
	{
		this(new Tuple<String, Float>("bludgeoning", 1.0f));
	}
	
	@SafeVarargs
	public DamageDistribution(Tuple<String, Float>... weights) 
	{
		super(weights);
		if(invariantViolated(this.distMap.values()))
		{
			throw new InvariantViolationException("weights are negative or do not add to 1!");
		}
	}
	
	public DamageDistribution(Map<String, Float> weightMap)
	{
		super(weightMap);
		if(invariantViolated(this.distMap.values()))
		{
			throw new InvariantViolationException("weights are negative or do not add to 1!");
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == DamageDistributionProvider.damageDist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == DamageDistributionProvider.damageDist ? DamageDistributionProvider.damageDist.<T> cast(this) : null;
	}

	@Override
	public Map<String, Float> distributeDamage(float dmg)
	{
		return super.distribute(dmg);
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IDamageDistribution.class, new DamageDistributionStorage(), new DamageDistributionFactory());
	}
	
	private static class DamageDistributionFactory implements Callable<IDamageDistribution>
	{
		@Override
		public IDamageDistribution call() throws Exception
		{
			return new DamageDistribution();
		}
	}

	private static class DamageDistributionStorage implements IStorage<IDamageDistribution>
	{

		@Override
		public NBTBase writeNBT(Capability<IDamageDistribution> capability, IDamageDistribution instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IDamageDistribution> capability, IDamageDistribution instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagList) nbt); 
		}
	}
}
