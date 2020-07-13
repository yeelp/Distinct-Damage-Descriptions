package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DamageDistribution extends Distribution implements IDamageDistribution
{	
	@Override
	protected boolean invariantViolated(float slash, float pierce, float bludge)
	{
		return slash + pierce + bludge > 1 && super.invariantViolated(slash, pierce, bludge);
	}
	
	public DamageDistribution()
	{
		this(0, 0, 1);
	}
	
	public DamageDistribution(float slash, float pierce, float bludge) 
	{
		super(slash, pierce, bludge);
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
	public DamageCategories distributeDamage(float dmg)
	{
		return new DamageCategories(super.distribute(dmg));
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
			instance.deserializeNBT((NBTTagCompound) nbt); 
		}
	}
}
