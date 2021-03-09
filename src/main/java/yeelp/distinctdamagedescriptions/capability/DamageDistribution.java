package yeelp.distinctdamagedescriptions.capability;

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
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.providers.DamageDistributionProvider;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DamageDistribution extends Distribution implements IDamageDistribution
{	
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
		this(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, 1.0f));
	}
	
	@SafeVarargs
	public DamageDistribution(Tuple<DDDDamageType, Float>... weights) 
	{
		super(weights);
		if(invariantViolated(this.distMap.values()))
		{
			throw new InvariantViolationException("weights are negative or do not add to 1!");
		}
	}
	
	public DamageDistribution(Map<DDDDamageType, Float> weightMap)
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
	public Map<DDDDamageType, Float> distributeDamage(float dmg)
	{
		if(ModConfig.dmg.useCustomDamageTypes)
		{
			return super.distribute(dmg);
		}
		else
		{
			NonNullMap<DDDDamageType, Float> map = new NonNullMap<DDDDamageType, Float>(0.0f);
			float remainingWeight = distMap.get(DDDBuiltInDamageType.BLUDGEONING) + distMap.get(DDDBuiltInDamageType.PIERCING) + distMap.get(DDDBuiltInDamageType.BLUDGEONING);
			long physicalDamageCount = distMap.keySet().stream().filter((s) -> s.getType() == DDDDamageType.Type.PHYSICAL).count();
			if(physicalDamageCount > 0)
			{
				remainingWeight /= physicalDamageCount;
				for(DDDDamageType type : DDDBuiltInDamageType.PHYSICAL_TYPES)
				{
					if(distMap.containsKey(type))
					{
						map.put(type, (distMap.get(type) + remainingWeight)*dmg);
					}
				}
			}
			else
			{
				map.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f);
			}
			return map;
		}
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
