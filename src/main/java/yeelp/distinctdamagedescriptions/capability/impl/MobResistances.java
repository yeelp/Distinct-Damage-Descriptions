package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.network.MobResistancesMessage;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class MobResistances extends DamageResistances implements IMobResistances {
	
	@CapabilityInject(IMobResistances.class)
	public static Capability<IMobResistances> cap;
	private boolean adaptive;
	private float adaptiveAmount, adaptiveAmountModified;
	private Set<DDDDamageType> adaptiveTo;

	public MobResistances() {
		this(new NonNullMap<DDDDamageType, Float>(() -> 0.0f), new HashSet<DDDDamageType>(), false, 0.0f);
	}

	public MobResistances(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities, boolean adaptitability, float adaptiveAmount) {
		super(resistances, immunities);
		this.adaptive = adaptitability;
		this.adaptiveAmount = adaptiveAmount;
		this.adaptiveAmountModified = adaptiveAmount;
		this.adaptiveTo = new HashSet<DDDDamageType>();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == cap ? cap.<T>cast(this) : null;
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
	public boolean isAdaptiveTo(DDDDamageType type) {
		return this.adaptiveTo.contains(type);
	}

	@Override
	public float getAdaptiveAmount() {
		return this.adaptiveAmount;
	}

	@Override
	public float getAdaptiveResistanceModified() {
		return this.adaptiveAmountModified;
	}

	@Override
	public void setAdaptiveAmount(float amount) {
		this.adaptiveAmount = amount;
	}

	@Override
	public float getResistance(DDDDamageType type) {
		return super.getResistance(type) + (this.adaptiveTo.contains(type) ? this.adaptiveAmountModified : 0);
	}

	@Override
	public boolean updateAdaptiveResistance(DamageMap dmgMap) {
		boolean sameKeys = this.adaptiveTo.stream().allMatch(dmgMap::containsKey) && dmgMap.keySet().stream().allMatch(this.adaptiveTo::contains);
		if(!sameKeys) {
			this.adaptiveTo = new HashSet<DDDDamageType>(dmgMap.keySet());
			if(ModConfig.core.enableAdaptiveWeakness) {
				float total = (float) YMath.sum(dmgMap.values());
				Map<DDDDamageType, Float> weightMap = dmgMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, (e) -> e.getValue()/total));
				float avgWeakness = (float) weightMap.entrySet().stream().filter((e) -> this.getResistance(e.getKey()) < 0).mapToDouble((e) -> this.getResistance(e.getKey())*e.getValue()).average().orElse(0);
				this.adaptiveAmountModified = (float) Math.exp(avgWeakness) * this.adaptiveAmount;
			}
		}
		return !sameKeys;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = super.serializeNBT();
		NBTTagList adaptabilityStatus = new NBTTagList();
		tag.setBoolean("adaptive", this.adaptive);
		tag.setFloat("adaptiveAmount", this.adaptiveAmount);
		tag.setFloat("adaptiveAmountModified", this.adaptiveAmountModified);
		for(DDDDamageType type : this.adaptiveTo) {
			adaptabilityStatus.appendTag(new NBTTagString(type.getTypeName()));
		}
		tag.setTag("adaptabilityStatus", adaptabilityStatus);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		super.deserializeNBT(tag);
		this.adaptive = tag.getBoolean("adaptive");
		this.adaptiveAmount = tag.getFloat("adaptiveAmount");
		this.adaptiveAmountModified = tag.getFloat("adaptiveAmountModified");
		if(this.adaptiveAmountModified == 0) {
			this.adaptiveAmountModified = this.adaptiveAmount;
		}
		this.adaptiveTo = new HashSet<DDDDamageType>();
		for(NBTBase nbt : tag.getTagList("adaptabilityStatus", new NBTTagString().getId())) {
			this.adaptiveTo.add(DDDRegistries.damageTypes.get(((NBTTagString) nbt).getString()));
		}
	}

	@Override
	public IMobResistances copy() {
		MobResistances copy = new MobResistances(super.copyMap(), super.copyImmunities(), this.adaptive, this.adaptiveAmount);
		copy.adaptiveTo = new HashSet<DDDDamageType>(this.adaptiveTo);
		return copy;
	}

	@Override
	public IMobResistances update(EntityLivingBase owner) {
		return this;
	}

	@Override
	public IMessage getIMessage() {
		return new MobResistancesMessage(this);
	}
}
