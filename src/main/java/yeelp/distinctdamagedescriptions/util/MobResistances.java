package yeelp.distinctdamagedescriptions.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class MobResistances extends DamageResistances implements IMobResistances
{
	private boolean adaptive;
	private float adaptiveAmount;
	private Set<String> adaptiveTo;
	
	public MobResistances()
	{
		this(new NonNullMap<String, Float>(0.0f), new HashSet<String>(), false, 0.0f);
	}
	
	public MobResistances(Map<String, Float> resistances, Collection<String> immunities, boolean adaptitability, float adaptiveAmount)
	{
		super(resistances, immunities);
		this.adaptive = adaptitability;
		this.adaptiveAmount = adaptiveAmount;
		this.adaptiveTo = new HashSet<String>();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == MobResistancesProvider.mobResist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == MobResistancesProvider.mobResist ? MobResistancesProvider.mobResist.<T> cast(this) : null;
	}

	@Override
	public boolean hasAdaptiveResistance()
	{
		return this.adaptive;
	}

	@Override
	public void setAdaptiveResistance(boolean status)
	{
		this.adaptive = status;
	}
	
	@Override
	public float getAdaptiveAmount()
	{
		return this.adaptiveAmount;
	}
	
	@Override
	public void setAdaptiveAmount(float amount)
	{
		this.adaptiveAmount = amount;
	}

	@Override
	public boolean updateAdaptiveResistance(String... damageTypes)
	{
		boolean changed = false;
		for(String s : damageTypes)
		{
			if(ModConfig.resist.adaptToCustom || DDDAPI.accessor.isPhysicalDamage(s))
			{
				if(adaptiveTo.contains(s))
				{
					continue;
				}
				else
				{
					changed = true;
					this.adaptiveTo.add(s);
					this.setResistance(s, this.getResistance(s) + adaptiveAmount);
				}
			}
		}
		Set<String> temp = new HashSet<String>(Arrays.asList(damageTypes));
		Set<String> iter = new HashSet<String>(adaptiveTo);
		for(String s : iter)
		{
			if(temp.contains(s))
			{
				continue;
			}
			else
			{
				changed = true;
				adaptiveTo.remove(s);
				this.setResistance(s, this.getResistance(s) - adaptiveAmount);
			}
		}
		return changed;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = super.serializeNBT();
		NBTTagList adaptabilityStatus = new NBTTagList();
		tag.setBoolean("adaptive", adaptive);
		tag.setFloat("adaptiveAmount", adaptiveAmount);
		for(String s : adaptiveTo)
		{
			adaptabilityStatus.appendTag(new NBTTagString(s));
		}
		tag.setTag("adaptabilityStatus", adaptabilityStatus);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		super.deserializeNBT(tag);
		adaptive = tag.getBoolean("adaptive");
		adaptiveAmount = tag.getFloat("adaptiveAmount");
		adaptiveTo = new HashSet<String>();
		for(NBTBase nbt : tag.getTagList("adaptabilityStatus", new NBTTagString().getId()))
		{
			adaptiveTo.add(((NBTTagString) nbt).getString());
		}
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IMobResistances.class, new MobResistancesStorage(), new MobResistancesFactory());
	}

	private static class MobResistancesFactory implements Callable<IMobResistances>
	{
		@Override
		public IMobResistances call() throws Exception
		{
			return new MobResistances();
		}
	}
	
	private static class MobResistancesStorage implements IStorage<IMobResistances>
	{
		@Override
		public NBTBase writeNBT(Capability<IMobResistances> capability, IMobResistances instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IMobResistances> capability, IMobResistances instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
