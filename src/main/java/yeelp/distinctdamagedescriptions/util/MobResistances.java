package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MobResistances extends DamageResistances implements IMobResistances
{
	private boolean adaptive;
	
	public MobResistances()
	{
		this(0, 0, 0, false, false, false, false);
	}
	
	public MobResistances(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, boolean adaptitveImmunity)
	{
		super(slashing, piercing, bludgeoning, slashImmune, pierceImmune, bludgeImmune);
		adaptive = adaptitveImmunity;
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
	public boolean hasAdaptiveImmunity()
	{
		return this.adaptive;
	}

	@Override
	public void setAdaptiveImmunity(boolean status)
	{
		this.adaptive = status;
	}

	@Override
	public void updateAdaptiveImmunity(DamageType... damageTypes)
	{
		super.setFullImmunity(false);
		for(DamageType damageType : damageTypes)
		{
			setImmunity(damageType, true);
		}
		if(damageTypes.length == DamageType.values().length)
		{
			int rand = (int) (3*Math.random());
			setImmunity(damageTypes[rand], false);
		}
	}

	private void setImmunity(DamageType damageType, boolean status)
	{
		switch(damageType)
		{
			case SLASHING:
				super.setSlashingImmunity(status);
				break;
			case PIERCING:
				super.setPiercingImmunity(status);
				break;
			case BLUDGEONING:
				super.setBludgeoningImmunity(status);
				break;
				//needed for JVM
			default:
				break;
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = super.serializeNBT();
		tag.setBoolean("adaptive", adaptive);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		super.deserializeNBT(tag);
		adaptive = tag.getBoolean("adaptive");
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
