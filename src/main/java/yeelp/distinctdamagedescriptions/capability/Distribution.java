package yeelp.distinctdamagedescriptions.capability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public abstract class Distribution implements IDistribution {
	protected DDDBaseMap<Float> distMap = new DDDBaseMap<Float>(0.0f);

	protected boolean invariantViolated(Collection<Float> weights) {
		for(float f : weights) {
			if(f >= 0.0f) {
				continue;
			}
			else {
				return true;
			}
		}
		return false;
	}

	@SafeVarargs
	Distribution(Tuple<DDDDamageType, Float>... weights) {
		for(Tuple<DDDDamageType, Float> t : weights) {
			if(t.getSecond() < 0.0f) {
				throw new InvariantViolationException("New weights are invalid!");
			}
			else if(t.getSecond() == 0.0f) // ignore 0 weighted entries.
			{
				continue;
			}
			else {
				distMap.put(t.getFirst(), t.getSecond());
			}
		}
	}

	Distribution(Map<DDDDamageType, Float> weightMap) {
		setNewMap(weightMap);
	}

	@Override
	public NBTTagList serializeNBT() {
		NBTTagList lst = new NBTTagList();
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", entry.getKey().getTypeName());
			tag.setFloat("weight", entry.getValue());
			lst.appendTag(tag);
		}
		return lst;
	}

	@Override
	public void deserializeNBT(NBTTagList lst) {
		this.distMap = new DDDBaseMap<Float>(0.0f);
		for(NBTBase nbt : lst) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			this.distMap.put(DDDRegistries.damageTypes.get(tag.getString("type")), tag.getFloat("weight"));
		}
	}

	@Override
	public float getWeight(DDDDamageType type) {
		return distMap.get(type);
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		distMap.put(type, amount);
	}

	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		if(invariantViolated(map.values())) {
			throw new InvariantViolationException("Weights are either non positive or do not add to 1!");
		}
		else {
			setNewMap(map);
		}
	}

	@Override
	public Set<DDDDamageType> getCategories() {
		HashSet<DDDDamageType> set = new HashSet<DDDDamageType>();
		for(Entry<DDDDamageType, Float> entry : distMap.entrySet()) {
			if((entry.getKey().getType() == DDDDamageType.Type.PHYSICAL || ModConfig.dmg.useCustomDamageTypes) && entry.getValue() > 0) {
				set.add(entry.getKey());
			}
		}
		return set;
	}

	<R, T extends DDDBaseMap<R>> T distribute(T map, Function<Float, R> valueFunc) {
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			map.put(entry.getKey(), valueFunc.apply(entry.getValue()));
		}
		return map;
	}

	private final void setNewMap(Map<DDDDamageType, Float> map) {
		map.entrySet().stream().filter((e) -> e.getValue() > 0).forEach((e) -> this.distMap.put(e.getKey(), e.getValue()));
	}
}
