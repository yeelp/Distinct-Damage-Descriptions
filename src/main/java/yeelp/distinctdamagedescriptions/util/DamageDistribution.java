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
	private boolean invariantViolated(float slash, float pierce, float bludge)
	{
		return slash + pierce + bludge > 1;
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
	
	@Override
	public void setNewWeights(float slashing, float piercing, float bludgeoning) throws InvariantViolationException
	{
		if(invariantViolated(slashing, piercing, bludgeoning))
		{
			throw new InvariantViolationException("New damage weights do not add to 1!");
		}
		else
		{
			super.setNewWeights(slashing, piercing, bludgeoning);
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
			instance.deserializeNBT((NBTTagCompound) nbt); 
		}
	}
}
