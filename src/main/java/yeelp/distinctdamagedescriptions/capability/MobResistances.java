package yeelp.distinctdamagedescriptions.capability;

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
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.providers.MobResistancesProvider;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class MobResistances extends DamageResistances implements IMobResistances {
	private boolean adaptive;
	private float adaptiveAmount;
	private Set<DDDDamageType> adaptiveTo;

	public MobResistances() {
		this(new NonNullMap<DDDDamageType, Float>(0.0f), new HashSet<DDDDamageType>(), false, 0.0f);
	}

	public MobResistances(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities, boolean adaptitability, float adaptiveAmount) {
		super(resistances, immunities);
		this.adaptive = adaptitability;
		this.adaptiveAmount = adaptiveAmount;
		this.adaptiveTo = new HashSet<DDDDamageType>();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == MobResistancesProvider.mobResist;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == MobResistancesProvider.mobResist ? MobResistancesProvider.mobResist.<T>cast(this) : null;
	}

	@Override
	public boolean hasAdaptiveResistance() {
		return this.adaptive;
	}

	@Override
	public void setAdaptiveResistance(boolean status) {
		this.adaptive = status;
	}

	@Override
	public float getAdaptiveAmount() {
		return this.adaptiveAmount;
	}

	@Override
	public void setAdaptiveAmount(float amount) {
		this.adaptiveAmount = amount;
	}

	@Override
	public boolean updateAdaptiveResistance(DDDDamageType... damageTypes) {
		boolean changed = false;
		for(DDDDamageType type : damageTypes) {
			if(ModConfig.resist.adaptToCustom || DDDAPI.accessor.isPhysicalDamage(type)) {
				if(adaptiveTo.contains(type)) {
					continue;
				}
				else {
					changed = true;
					this.adaptiveTo.add(type);
					this.setResistance(type, this.getResistance(type) + adaptiveAmount);
				}
			}
		}
		Set<DDDDamageType> temp = new HashSet<DDDDamageType>(Arrays.asList(damageTypes));
		Set<DDDDamageType> iter = new HashSet<DDDDamageType>(adaptiveTo);
		for(DDDDamageType type : iter) {
			if(temp.contains(type)) {
				continue;
			}
			else {
				changed = true;
				adaptiveTo.remove(type);
				this.setResistance(type, this.getResistance(type) - adaptiveAmount);
			}
		}
		return changed;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = super.serializeNBT();
		NBTTagList adaptabilityStatus = new NBTTagList();
		tag.setBoolean("adaptive", adaptive);
		tag.setFloat("adaptiveAmount", adaptiveAmount);
		for(DDDDamageType type : adaptiveTo) {
			adaptabilityStatus.appendTag(new NBTTagString(type.getTypeName()));
		}
		tag.setTag("adaptabilityStatus", adaptabilityStatus);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		super.deserializeNBT(tag);
		adaptive = tag.getBoolean("adaptive");
		adaptiveAmount = tag.getFloat("adaptiveAmount");
		adaptiveTo = new HashSet<DDDDamageType>();
		for(NBTBase nbt : tag.getTagList("adaptabilityStatus", new NBTTagString().getId())) {
			adaptiveTo.add(DDDRegistries.damageTypes.get(((NBTTagString) nbt).getString()));
		}
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IMobResistances.class, new MobResistancesStorage(), new MobResistancesFactory());
	}

	private static class MobResistancesFactory implements Callable<IMobResistances> {
		@Override
		public IMobResistances call() throws Exception {
			return new MobResistances();
		}
	}

	private static class MobResistancesStorage implements IStorage<IMobResistances> {
		@Override
		public NBTBase writeNBT(Capability<IMobResistances> capability, IMobResistances instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IMobResistances> capability, IMobResistances instance, EnumFacing side, NBTBase nbt) {
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	@Override
	public IDamageResistances copy() {
		MobResistances copy = new MobResistances(super.copyMap(), super.copyImmunities(), this.adaptive, this.adaptiveAmount);
		copy.adaptiveTo = new HashSet<DDDDamageType>(this.adaptiveTo);
		return copy;
	}
}
