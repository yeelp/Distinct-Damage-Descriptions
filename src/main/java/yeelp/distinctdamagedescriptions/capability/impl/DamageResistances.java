package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageResistances;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

/**
 * Base capability for damage resistance capabilities
 * 
 * @author Yeelp
 *
 */
public abstract class DamageResistances implements IDamageResistances {
	private DDDBaseMap<Float> resistances;
	private Set<DDDDamageType> immunities;

	DamageResistances(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities) {
		this.resistances = resistances.keySet().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, resistances::get));
		this.immunities = new HashSet<DDDDamageType>(immunities);
	}

	@Override
	public float getResistance(DDDDamageType type) {
		return this.resistances.get(type);
	}

	@Override
	public boolean hasImmunity(DDDDamageType type) {
		return this.immunities.contains(type);
	}

	@Override
	public void setResistance(DDDDamageType type, float amount) {
		this.resistances.put(type, amount);
	}	

	@Override
	public void removeResistance(DDDDamageType type) {
		this.resistances.remove(type);
	}

	@Override
	public void setImmunity(DDDDamageType type, boolean status) {
		if(status) {
			this.immunities.add(type);
		}
		else {
			this.immunities.remove(type);
		}
	}

	@Override
	public void clearImmunities() {
		this.immunities.clear();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList lst = new NBTTagList();
		NBTTagList immunities = new NBTTagList();
		for(Entry<DDDDamageType, Float> entry : this.resistances.entrySet()) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("type", entry.getKey().getTypeName());
			compound.setFloat("amount", entry.getValue());
			lst.appendTag(compound);
		}
		tag.setTag("resistances", lst);
		for(DDDDamageType type : this.immunities) {
			immunities.appendTag(new NBTTagString(type.getTypeName()));
		}
		tag.setTag("immunities", immunities);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		this.resistances = new DDDBaseMap<Float>(() -> 0.0f);
		this.immunities = new HashSet<DDDDamageType>();
		for(NBTBase nbt : tag.getTagList("resistances", new NBTTagCompound().getId())) {
			NBTTagCompound resist = (NBTTagCompound) nbt;
			this.resistances.put(validateNonNull(DDDRegistries.damageTypes.get(resist.getString("type"))), resist.getFloat("amount"));
		}
		for(NBTBase nbt : tag.getTagList("immunities", new NBTTagString().getId())) {
			this.immunities.add(validateNonNull(DDDRegistries.damageTypes.get(((NBTTagString) nbt).getString())));
		}
	}

	protected Set<DDDDamageType> copyImmunities() {
		return new HashSet<DDDDamageType>(this.immunities);
	}

	protected DDDBaseMap<Float> copyMap() {
		return this.resistances.keySet().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, this.resistances::get));
	}
	
	private static <T> T validateNonNull(T t) {
		return Objects.requireNonNull(t, t + " isn't a valid registered damage type!");
	}
}
