package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ArmorDistribution extends Distribution implements IArmorDistribution
{
	public ArmorDistribution()
	{
		this(0, 0, 0);
	}
	
	public ArmorDistribution(float slashing, float piercing, float bludgeoning)
	{
		super(slashing, piercing, bludgeoning);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ArmorDistributionProvider.armorResist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ArmorDistributionProvider.armorResist ? ArmorDistributionProvider.armorResist.<T> cast(this) : null;
	}

	@Override
	public ArmorCategories distributeArmor(float armor, float toughness)
	{
		return new ArmorCategories(super.distribute(armor), super.distribute(toughness));
	}

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IArmorDistribution.class, new ArmorResistancesStorage(), new ArmorResistancesFactory());
	}
	
	private static class ArmorResistancesFactory implements Callable<IArmorDistribution>
	{
		@Override
		public IArmorDistribution call() throws Exception
		{
			return new ArmorDistribution();
		}
	}
	
	private static class ArmorResistancesStorage implements IStorage<IArmorDistribution>
	{
		@Override
		public NBTBase writeNBT(Capability<IArmorDistribution> capability, IArmorDistribution instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IArmorDistribution> capability, IArmorDistribution instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
