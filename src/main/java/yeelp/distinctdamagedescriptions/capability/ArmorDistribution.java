package yeelp.distinctdamagedescriptions.capability;

import java.util.Map;
import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.providers.ArmorDistributionProvider;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class ArmorDistribution extends Distribution implements IArmorDistribution
{
	public ArmorDistribution()
	{
		this(new NonNullMap<DDDDamageType, Float>(0.0f));
	}
	
	@SafeVarargs
	public ArmorDistribution(Tuple<DDDDamageType, Float>...weights)
	{
		super(weights);
	}
	
	public ArmorDistribution(Map<DDDDamageType, Float> resistMap)
	{
		super(resistMap);
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
	public ArmorMap distributeArmor(float armor, float toughness)
	{
		return super.distribute(new ArmorMap(), (f) -> new ArmorValues(armor*f, toughness*f));
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
			instance.deserializeNBT((NBTTagList) nbt);
		}
	}
}
