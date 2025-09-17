package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageResistances;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.NonNullSet;

/**
 * Base capability for damage resistance capabilities
 * 
 * @author Yeelp
 *
 */
public abstract class DamageResistances extends AbstractUpdatableCapability<NBTTagCompound> implements IDamageResistances {
	private final DDDBaseMap<Float> resistances, originalResistances;
	private final NonNullSet<DDDDamageType> immunities, originalImmunities;
	
	private static final String TYPE_KEY = "type";
	private static final String AMOUNT_KEY = "amount";
	private static final String RESISTANCES_KEY = "resistances";
	private static final String IMMUNITIES_KEY = "immunities";
	private static final String ORIGINAL_RESISTANCES_KEY = "originalResistances";
	private static final String ORIGINAL_IMMUNITIES_KEY = "originalImmunities";
	

	DamageResistances(Map<DDDDamageType, Float> resistances, Collection<DDDDamageType> immunities) {
		this.resistances = resistances.keySet().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, resistances::get));
		this.immunities = NonNullSet.newNonNullSetRemoveNulls(immunities);
		(this.originalResistances = new DDDBaseMap<Float>(() -> 0.0f)).putAll(this.resistances);
		this.originalImmunities = new NonNullSet<DDDDamageType>(this.immunities);
	}

	@Override
	public float getResistance(DDDDamageType type) {
		return this.resistances.get(type);
	}
	
	@Override
	public float getBaseResistance(DDDDamageType type) {
		return this.originalResistances.get(type);
	}

	@Override
	public boolean hasImmunity(DDDDamageType type) {
		return this.immunities.contains(type);
	}
	
	@Override
	public boolean hasBaseImmunity(DDDDamageType type) {
		return this.originalImmunities.contains(type);
	}

	@Override
	public void setResistance(DDDDamageType type, float amount) {
		this.resistances.put(type, amount);
	}
	
	@Override
	public void setBaseResistance(DDDDamageType type, float value) {
		this.originalResistances.put(type, value);
	}

	@Override
	public void removeResistance(DDDDamageType type) {
		this.resistances.remove(type);
	}
	
	@Override
	public void removeBaseResistance(DDDDamageType type) {
		this.originalResistances.remove(type);
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
	public void setBaseImmunity(DDDDamageType type, boolean status) {
		if(status) {
			this.originalImmunities.add(type);
		}
		else {
			this.originalImmunities.remove(type);
		}
	}

	@Override
	public void clearImmunities() {
		this.immunities.clear();
	}
	
	@Override
	public void clearBaseImmunities() {
		this.originalImmunities.clear();
	}
	
	@Override
	public void clearResistances() {
		this.resistances.clear();
	}
	
	@Override
	public void clearBaseResistances() {
		this.originalResistances.clear();
	}

	@Override
	public NBTTagCompound serializeSpecificNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList lst = resistancesToNBT(this.resistances);
		NBTTagList originalLst = resistancesToNBT(this.originalResistances);
		NBTTagList immunities = immunitiesToNBT(this.immunities);
		NBTTagList originalImmunities = immunitiesToNBT(this.originalImmunities);
		tag.setTag(RESISTANCES_KEY, lst);
		tag.setTag(ORIGINAL_RESISTANCES_KEY, originalLst);
		tag.setTag(IMMUNITIES_KEY, immunities);
		tag.setTag(ORIGINAL_IMMUNITIES_KEY, originalImmunities);
		return tag;
	}

	@Override
	public void deserializeSpecificNBT(NBTTagCompound tag) {
		this.resistances.clear();
		this.immunities.clear();
		this.originalImmunities.clear();
		this.originalResistances.clear();
		resistancesFromNBT(this.resistances, tag.getTagList(RESISTANCES_KEY, NBT.COMPOUND_TAG_ID));
		resistancesFromNBT(this.originalResistances, tag.getTagList(ORIGINAL_RESISTANCES_KEY, NBT.COMPOUND_TAG_ID));
		immunitiesFromNBT(this.immunities, tag.getTagList(IMMUNITIES_KEY, NBT.STRING_TAG_ID));
		immunitiesFromNBT(this.originalImmunities, tag.getTagList(ORIGINAL_IMMUNITIES_KEY, NBT.STRING_TAG_ID));
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
	
	private static NBTTagList resistancesToNBT(DDDBaseMap<Float> map) {
		NBTTagList lst = new NBTTagList();
		map.forEach((type, amount) -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString(TYPE_KEY, type.getTypeName());
			tag.setFloat(AMOUNT_KEY, amount);
			lst.appendTag(tag);
		});
		return lst;
	}
	
	private static void resistancesFromNBT(DDDBaseMap<Float> map, NBTTagList lst) {
		lst.forEach((nbt) -> {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			map.put(validateNonNull(DDDRegistries.damageTypes.get(tag.getString(TYPE_KEY))), tag.getFloat(AMOUNT_KEY));
		});
	}
	
	private static NBTTagList immunitiesToNBT(Set<DDDDamageType> types) {
		NBTTagList lst = new NBTTagList();
		types.forEach((type) -> lst.appendTag(new NBTTagString(type.getTypeName())));
		return lst;
	}
	
	private static void immunitiesFromNBT(NonNullSet<DDDDamageType> types, NBTTagList lst) {
		lst.forEach((nbt) -> types.add(validateNonNull(DDDRegistries.damageTypes.get(((NBTTagString) nbt).getString()))));
	}
	
	protected final void resetToOriginals() {
		this.clearImmunities();
		this.clearResistances();
		this.resistances.putAll(this.originalResistances);
		this.immunities.addAll(this.originalImmunities);
	}
}
