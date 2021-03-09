package yeelp.distinctdamagedescriptions.capability;

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
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.providers.ShieldDistributionProvider;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ShieldDistribution extends Distribution implements IDistribution
{
	public ShieldDistribution()
	{
		this(new NonNullMap<DDDDamageType, Float>(1.0f));
	}
	
	public ShieldDistribution(Map<DDDDamageType, Float> blockMap)
	{
		super(blockMap);
	}
	
	@SafeVarargs
	public ShieldDistribution(Tuple<DDDDamageType, Float>... mappings)
	{
		super(mappings);
	}
	
	public Map<DDDDamageType, Float> block(Map<DDDDamageType, Float> fullDamage)
	{
		Map<DDDDamageType, Float> remainingDamage = new NonNullMap<DDDDamageType, Float>(0.0f);
		for(Entry<DDDDamageType, Float> entry : fullDamage.entrySet())
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