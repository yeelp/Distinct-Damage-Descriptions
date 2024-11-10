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
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class MobResistances extends DamageResistances implements IMobResistances {

	@CapabilityInject(IMobResistances.class)
	public static Capability<IMobResistances> cap;
	private boolean adaptive, adaptiveOriginally;
	private float adaptiveAmount, adaptiveAmountModified, adaptiveAmountOriginal;
	private Set<DDDDamageType> adaptiveTo;
	
	private static final String ADAPTIVE_KEY = "adaptive";
	private static final String ADAPTIVE_ORIGINAL_KEY = "adaptiveOriginally";
	private static final String ADAPTIVE_AMOUNT_KEY = "adaptiveAmount";
	private static final String ADAPTIVE_AMOUNT_MODIFIED_KEY = "adaptiveAmountModified";
	private static final String ADAPTIVE_AMOUNT_ORIGINAL_KEY = "adaptiveAmountOriginal";
	private static final String ADAPTABILITY_STATUS = "adaptabilityStatus";

	public MobResistances() {
		this(new DDDBaseMap<Float>(() -> 0.0f), new HashSet<DDDDamageType>(), false, 0.0f);
	}

	public MobResistances(DDDBaseMap<Float> resistances, Collection<DDDDamageType> immunities, boolean adaptability, float adaptiveAmount) {
		super(resistances, immunities);
		this.adaptive = adaptability;
		this.adaptiveOriginally = adaptability;
		this.adaptiveAmount = adaptiveAmount;
		this.adaptiveAmountModified = adaptiveAmount;
		this.adaptiveAmountOriginal = adaptiveAmount;
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
	public boolean isOriginallyAdaptive() {
		return this.adaptiveOriginally;
	}

	@Override
	public void setAdaptiveResistance(boolean status, boolean permanent) {
		if(permanent) {
			this.adaptiveOriginally = status;
		}
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
	public float getBaseAdaptiveAmount() {
		return this.adaptiveAmountOriginal;
	}

	@Override
	public float getAdaptiveResistanceModified() {
		return this.adaptiveAmountModified;
	}

	@Override
	public void setAdaptiveAmount(float amount) {
		float lastAvgWeakness = 0;
		if(ModConfig.core.enableAdaptiveWeakness) {
			lastAvgWeakness = this.computeLastAverageWeakness();
		}
		this.adaptiveAmount = amount;
		this.computeAndSetAdaptiveWeakness(lastAvgWeakness);
	}
	
	@Override
	public void setBaseAdaptiveAmount(float amount) {
		this.adaptiveAmountOriginal = amount;
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
				Map<DDDDamageType, Float> weightMap = dmgMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, (e) -> e.getValue() / total));
				float avgWeakness = (float) weightMap.entrySet().stream().filter((e) -> this.getResistance(e.getKey()) < 0).mapToDouble((e) -> this.getResistance(e.getKey()) * e.getValue()).average().orElse(0);
				this.computeAndSetAdaptiveWeakness(avgWeakness);
			}
		}
		return !sameKeys;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound tag = (NBTTagCompound) super.serializeNBT();
		NBTTagList adaptabilityStatus = new NBTTagList();
		tag.setBoolean(ADAPTIVE_KEY, this.adaptive);
		tag.setBoolean(ADAPTIVE_ORIGINAL_KEY, this.adaptiveOriginally);
		tag.setFloat(ADAPTIVE_AMOUNT_KEY, this.adaptiveAmount);
		tag.setFloat(ADAPTIVE_AMOUNT_MODIFIED_KEY, this.adaptiveAmountModified);
		tag.setFloat(ADAPTIVE_AMOUNT_ORIGINAL_KEY, this.adaptiveAmountOriginal);
		for(DDDDamageType type : this.adaptiveTo) {
			adaptabilityStatus.appendTag(new NBTTagString(type.getTypeName()));
		}
		tag.setTag(ADAPTABILITY_STATUS, adaptabilityStatus);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		super.deserializeNBT(tag);
		if(tag instanceof NBTTagCompound) {
			this.deserializeMobResistances((NBTTagCompound) tag);
		}
	}
	
	@Override
	public void deserializeOldNBT(NBTBase nbt) {
		this.deserializeMobResistances((NBTTagCompound) nbt);
	}

	@Override
	public IMobResistances copy() {
		MobResistances copy = new MobResistances(super.copyMap(), super.copyImmunities(), this.adaptive, this.adaptiveAmount);
		copy.adaptiveTo = new HashSet<DDDDamageType>(this.adaptiveTo);
		return copy;
	}

	@Override
	public IMobResistances update(EntityLivingBase owner) {
		Set<String> mods = DDDRegistries.modifiers.getEntityResistancesRegistry().getNamesOfApplicableModifiers(owner);
		if(!YMath.setEquals(mods, this.getModifiers())) {
			this.resetToOriginals();
			this.adaptive = this.adaptiveOriginally;
			this.adaptiveAmount = this.adaptiveAmountOriginal;
			float lastAvgWeakness = 0.0f;
			if(ModConfig.core.enableAdaptiveWeakness) {
				lastAvgWeakness = this.computeLastAverageWeakness();
			}
			DDDRegistries.modifiers.getEntityResistancesRegistry().applyModifiers(owner, this);
			this.computeAndSetAdaptiveWeakness(lastAvgWeakness);
			this.updateModifiers(mods);
		}
		return this;
	}

	@Override
	public IMessage getIMessage() {
		return new MobResistancesMessage(this);
	}
	
	@Override
	public Class<NBTTagCompound> getSpecificNBTClass() {
		return NBTTagCompound.class;
	}
	
	private float computeAndSetAdaptiveWeakness(float avgWeakness) {
		return this.adaptiveAmountModified = (float) Math.exp(avgWeakness) * this.adaptiveAmount;
	}
	
	private float computeLastAverageWeakness() {
		return (float) Math.log(this.adaptiveAmountModified / this.adaptiveAmount);
	}
	
	private void deserializeMobResistances(NBTTagCompound compound) {
		this.adaptive = compound.getBoolean(ADAPTIVE_KEY);
		if(compound.hasKey(ADAPTIVE_AMOUNT_ORIGINAL_KEY)) {
			this.adaptiveOriginally = compound.getBoolean(ADAPTIVE_ORIGINAL_KEY);			
		}
		this.adaptiveOriginally = this.adaptive;
		this.adaptiveAmount = compound.getFloat(ADAPTIVE_AMOUNT_KEY);
		this.adaptiveAmountModified = compound.getFloat(ADAPTIVE_AMOUNT_MODIFIED_KEY);
		this.adaptiveAmountOriginal = compound.getFloat(ADAPTIVE_AMOUNT_ORIGINAL_KEY);
		if(this.adaptiveAmountModified == 0) {
			this.adaptiveAmountModified = this.adaptiveAmount;
		}
		if(this.adaptiveAmountOriginal == 0) {
			this.adaptiveAmountOriginal = this.adaptiveAmount;
		}
		this.adaptiveTo = new HashSet<DDDDamageType>();
		for(NBTBase nbt : compound.getTagList(ADAPTABILITY_STATUS, new NBTTagString().getId())) {
			this.adaptiveTo.add(DDDRegistries.damageTypes.get(((NBTTagString) nbt).getString()));
		}
	}
}
