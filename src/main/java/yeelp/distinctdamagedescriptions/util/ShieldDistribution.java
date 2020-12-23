package yeelp.distinctdamagedescriptions.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ShieldDistribution extends Distribution implements IDistribution
{
	public ShieldDistribution()
	{
		this(new NonNullMap<String, Float>(1.0f));
	}
	
	public ShieldDistribution(Map<String, Float> blockMap)
	{
		super(blockMap);
	}
	
	@SafeVarargs
	public ShieldDistribution(Tuple<String, Float>... mappings)
	{
		super(mappings);
	}
	
	public Map<String, Float> block(Map<String, Float> fullDamage)
	{
		Map<String, Float> remainingDamage = new NonNullMap<String, Float>(0.0f);
		for(Entry<String, Float> entry : fullDamage.entrySet())
		{
			remainingDamage.put(entry.getKey(), blockDamage(entry.getValue(), this.getWeight(entry.getKey())));
		}
		return remainingDamage;
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ShieldDistribution.class, new ShieldDistributionStorage(), new ShieldDistributionFactory());
	}


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ShieldDistributionProvider.shieldDist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ShieldDistributionProvider.shieldDist ? ShieldDistributionProvider.shieldDist.<T> cast(this) : null;
	}
	
	private float blockDamage(float damage, float weight)
	{
		return MathHelper.clamp(damage*(1-weight), 0, Float.MAX_VALUE);
	}
	
	private static class ShieldDistributionFactory implements Callable<ShieldDistribution>
	{
		@Override
		public ShieldDistribution call() throws Exception
		{
			return new ShieldDistribution();
		}	
	}
	
	private static class ShieldDistributionStorage implements IStorage<ShieldDistribution>
	{
		@Override
		public NBTBase writeNBT(Capability<ShieldDistribution> capability, ShieldDistribution instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ShieldDistribution> capability, ShieldDistribution instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagList) nbt);
		}
	}
}