package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ArmorResistances extends Distribution implements IArmorResistances
{
	public ArmorResistances()
	{
		this(0, 0, 0);
	}
	
	public ArmorResistances(float slashing, float piercing, float bludgeoning)
	{
		super(slashing, piercing, bludgeoning);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ArmorResistancesProvider.armorResist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ArmorResistancesProvider.armorResist ? ArmorResistancesProvider.armorResist.<T> cast(this) : null;
	}

	@Override
	public ArmorCategories distributeArmor(float armor, float toughness)
	{
		return new ArmorCategories(super.distribute(armor), super.distribute(toughness));
	}

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IArmorResistances.class, new ArmorResistancesStorage(), new ArmorResistancesFactory());
	}
	
	private static class ArmorResistancesFactory implements Callable<IArmorResistances>
	{
		@Override
		public IArmorResistances call() throws Exception
		{
			return new ArmorResistances();
		}
	}
	
	private static class ArmorResistancesStorage implements IStorage<IArmorResistances>
	{
		@Override
		public NBTBase writeNBT(Capability<IArmorResistances> capability, IArmorResistances instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IArmorResistances> capability, IArmorResistances instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
