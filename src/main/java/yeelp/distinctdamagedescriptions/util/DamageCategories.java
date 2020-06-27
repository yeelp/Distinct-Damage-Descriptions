package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Implementation of IDamageCategories capability
 * @author Yeelp
 *
 */
public class DamageCategories implements IDamageCategories 
{
	private float slashing;
	private float piercing;
	private float bludgeoning;
	
	/**
	 * Create a new DamageCategories initialized to zero for all types
	 */
	public DamageCategories()
	{
		this.bludgeoning = 0.0f;
		this.piercing = 0.0f;
		this.slashing = 0.0f;
	}
	
	/**
	 * Create a new DamageCateogires with specified amount for each type
	 * @param slashing slashing amount
	 * @param piercing piercing amount
	 * @param bludgeoning bludgeoning amount
	 */
	public DamageCategories(float slashing, float piercing, float bludgeoning)
	{
		this.bludgeoning = bludgeoning;
		this.slashing = slashing;
		this.piercing = piercing;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == DamageCategoriesProvider.damageCategories;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == DamageCategoriesProvider.damageCategories ? DamageCategoriesProvider.damageCategories.<T> cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("slashing", slashing);
		tag.setFloat("bludgeoning", bludgeoning);
		tag.setFloat("piercing", piercing);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.slashing = nbt.getFloat("slashing");
		this.bludgeoning = nbt.getFloat("bludgeoning");
		this.piercing = nbt.getFloat("piercing");
	}

	@Override
	public float getDamage(DamageType type)
	{
		switch(type)
		{
			case BLUDGEONING:
				return this.bludgeoning;
			case PIERCING:
				return this.piercing;
			case SLASHING:
				return this.slashing;
			//needed for JVM
			default:
				return 0.0f;
		}
	}

	@Override
	public void setDamage(DamageType type, float amount)
	{
		switch(type)
		{
			case BLUDGEONING:
				this.bludgeoning = amount;
				break;
			case PIERCING:
				this.piercing = amount;
				break;
			case SLASHING:
				this.slashing = amount;
				break;
			//needed for JVM
			default:
				break;
		}
	}

	/**
	 * register this capability
	 */
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IDamageCategories.class, new DamageCategoriesStorage(), new DamageCategoriesFactory());
	}
	
	private static class DamageCategoriesFactory implements Callable<IDamageCategories>
	{
		@Override
		public IDamageCategories call() throws Exception
		{
			return new DamageCategories();
		}
	}
	
	private static class DamageCategoriesStorage implements IStorage<IDamageCategories>
	{

		@Override
		public NBTBase writeNBT(Capability<IDamageCategories> capability, IDamageCategories instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IDamageCategories> capability, IDamageCategories instance, EnumFacing side, NBTBase nbt)
		{
			if(nbt instanceof NBTTagCompound)
			{
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
		}
	}	
}
