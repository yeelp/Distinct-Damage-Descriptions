package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DamageDistribution implements IDamageDistribution
{
	private float slash;
	private float pierce;
	private float bludge;
	
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
		this.slash = slash;
		this.pierce = pierce;
		this.bludge = bludge;
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
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("slashing", slash);
		tag.setFloat("piercing", pierce);
		tag.setFloat("bludgeoning", bludge);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		slash = nbt.getFloat("slashing");
		pierce = nbt.getFloat("piercing");
		bludge = nbt.getFloat("bludgeoning");
	}

	@Override
	public DamageCategories distributeDamage(float dmg)
	{
		return new DamageCategories(dmg*slash, dmg*pierce, dmg*bludge);
	}

	@Override
	public float getSlashingWeight()
	{
		return slash;
	}

	@Override
	public float getPiercingWeight()
	{
		return pierce;
	}

	@Override
	public float getBludgeoningWeight()
	{
		return bludge;
	}

	@Override
	public void setNewWeights(float slash, float pierce, float bludgeoning) throws InvariantViolationException
	{
		if(invariantViolated(slash, pierce, bludgeoning))
		{
			throw new InvariantViolationException("New damage weights do not add to 1!");
		}
		else
		{
			this.slash = slash;
			this.pierce = pierce;
			this.bludge = bludgeoning;
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
