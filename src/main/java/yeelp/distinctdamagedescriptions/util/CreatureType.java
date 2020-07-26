package yeelp.distinctdamagedescriptions.util;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.registries.DDDCreatureTypeRegistries;

public class CreatureType implements ICreatureType
{
	public static final CreatureType UNKNOWN = new CreatureType(CreatureTypeData.UNKNOWN, CreatureTypeData.UNKNOWN);
	public String mainType, subType;
	
	public CreatureType(CreatureTypeData main, CreatureTypeData sub)
	{
		mainType = main.getTypeName();
		subType = sub.getTypeName();
	}
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CreatureTypeProvider.creatureType;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == CreatureTypeProvider.creatureType ? CreatureTypeProvider.creatureType.<T> cast(this) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("mainType", mainType);
		tag.setString("subType", subType);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		mainType = nbt.getString("mainType");
		subType = nbt.getString("subType");
	}

	@Override
	public CreatureTypeData getMainCreatureTypeData()
	{
		return DDDCreatureTypeRegistries.getCreatureTypeData(mainType);
	}

	@Override
	public CreatureTypeData getSubCreatureTypeData()
	{
		return DDDCreatureTypeRegistries.getCreatureTypeData(subType);
	}
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ICreatureType.class, new CreatureTypeStorage(), new CreatureTypeFactory());
	}
	
	private static class CreatureTypeFactory implements Callable<ICreatureType>
	{
		@Override
		public ICreatureType call() throws Exception
		{
			return CreatureType.UNKNOWN;
		}
	}
	
	private static class CreatureTypeStorage implements IStorage<ICreatureType>
	{
		@Override
		public NBTBase writeNBT(Capability<ICreatureType> capability, ICreatureType instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ICreatureType> capability, ICreatureType instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}	
	}
}
