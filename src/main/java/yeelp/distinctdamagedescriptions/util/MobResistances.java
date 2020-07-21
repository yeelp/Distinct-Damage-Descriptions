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
	private float adaptiveAmount;
	private boolean[] adaptiveTo;
	
	public MobResistances()
	{
		this(0, 0, 0, false, false, false, false, 0.0f);
	}
	
	public MobResistances(float slashing, float piercing, float bludgeoning, boolean slashImmune, boolean pierceImmune, boolean bludgeImmune, boolean adaptitability, float adaptiveAmount)
	{
		super(slashing, piercing, bludgeoning, slashImmune, pierceImmune, bludgeImmune);
		this.adaptive = adaptitability;
		this.adaptiveAmount = adaptiveAmount;
		this.adaptiveTo = new boolean[DamageType.values().length];
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
	public boolean updateAdaptiveResistance(DamageType... damageTypes)
	{
		float[] netChange = new float[3];
		for(DamageType damageType : DamageType.values())
		{
			int i = damageType.ordinal();
			if(adaptiveTo[i])
			{
				adaptiveTo[i] = false;
				netChange[i] -= adaptiveAmount;
			}
		}
		for(DamageType damageType : damageTypes)
		{
			int i = damageType.ordinal();
			adaptiveTo[i] = true;
			netChange[i] += adaptiveAmount;
		}
		super.slashing += netChange[0];
		super.piercing += netChange[1];
		super.bludgeoning += netChange[2];
		return netChange[0] == 0 && netChange[1] == 0 && netChange[2] == 0;
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
		tag.setFloat("adaptiveAmount", adaptiveAmount);
		byte[] bytes = new byte[adaptiveTo.length];
		for(int i = 0; i < adaptiveTo.length; i++)
		{
			bytes[i] = booleanAsByte(adaptiveTo[i]);
		}
		tag.setByteArray("adaptabilityStatus", bytes);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		super.deserializeNBT(tag);
		adaptive = tag.getBoolean("adaptive");
		adaptiveAmount = tag.getFloat("adaptiveAmount");
		int i = 0;
		for(byte b : tag.getByteArray("adaptabilityStatus"))
		{
			adaptiveTo[i++] = byteAsBoolean(b);
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
	
	private static byte booleanAsByte(boolean b)
	{
		return (byte) (b ? 1 : 0);
	}
	
	private static boolean byteAsBoolean(byte b)
	{
		return b != 0;
	}
}
