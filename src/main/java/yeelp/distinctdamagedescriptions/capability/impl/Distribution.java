package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public abstract class Distribution implements IDistribution {
	protected DDDBaseMap<Float> distMap = new DDDBaseMap<Float>(() -> 0.0f);

	protected static boolean invariantViolated(Collection<Float> weights) {
		for(float f : weights) {
			if(f >= 0.0f) {
				continue;
			}
			return true;
		}
		return false;
	}

	@SafeVarargs
	Distribution(Tuple<DDDDamageType, Float>... weights) {
		for(Tuple<DDDDamageType, Float> t : weights) {
			if(t.getSecond() < 0.0f) {
				throw new InvariantViolationException("New weights are invalid!");
			}
			else if(t.getSecond() == 0.0f && !this.allowZeroWeightedEntries()) // ignore 0 weighted entries.
			{
				continue;
			}
			else {
				this.distMap.put(t.getFirst(), t.getSecond());
			}
		}
	}

	protected Distribution(Map<DDDDamageType, Float> weightMap) {
		setNewMap(weightMap);
	}

	@Override
	public NBTTagList serializeNBT() {
		return DDDBaseMap.toNBT(this.distMap);
	}

	@Override
	public void deserializeNBT(NBTTagList lst) {
		this.distMap = DDDBaseMap.fromNBT(lst, () -> 0.0f);
	}

	@Override
	public float getWeight(DDDDamageType type) {
		return this.distMap.get(type);
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		if(amount < 0) {
			throw new InvariantViolationException("Can't set negative weight!");
		}
		if(!this.allowZeroWeightedEntries() && amount == 0.0f) {
			throw new InvariantViolationException("This Distribution doesn't support zero weighted entries!");
		}
		this.distMap.put(type, amount);
	}

	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		if(invariantViolated(map.values())) {
			throw new InvariantViolationException("Weights are either non positive or do not add to 1!");
		}
		setNewMap(map);
	}

	@Override
	public Set<DDDDamageType> getCategories() {
		HashSet<DDDDamageType> set = new HashSet<DDDDamageType>();
		this.distMap.forEach((type, weight) -> {
			if(type.isUsable() && (weight > 0.0f || (weight == 0.0f && this.allowZeroWeightedEntries()))) {
				set.add(type);
			}
		});
		return set;
	}

	<R, T extends DDDBaseMap<R>> T distribute(T map, Function<Float, R> valueFunc) {
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			map.put(entry.getKey(), valueFunc.apply(entry.getValue()));
		}
		return map;
	}

	private final void setNewMap(Map<DDDDamageType, Float> map) {
		this.distMap.clear();
		map.entrySet().stream().filter((e) -> {
			float val = e.getValue();
			return val > 0 || (val == 0.0f && this.allowZeroWeightedEntries());
		}).forEach((e) -> this.distMap.put(e.getKey(), e.getValue()));
	}

	protected final DDDBaseMap<Float> copyMap(float defaultVal) {
		return this.distMap.entrySet().stream().collect(() -> new DDDBaseMap<Float>(() -> defaultVal), (m, e) -> m.put(e.getKey(), e.getValue()), DDDBaseMap::putAll);
	}

	protected static final <Dist extends Distribution & IDistribution> DDDBaseMap<Float> copyMap(Dist dist) {
		return dist.copyMap(dist.distMap.getDefaultValue());
	}
	
	protected abstract boolean allowZeroWeightedEntries();
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		builder.append(YLib.joinNiceString(true, ",", this.distMap.entrySet().stream().map((e) -> String.format("(%s, %.2f)", e.getKey().getTypeName(), e.getValue())).toArray(String[]::new)));
		return builder.append("]").toString();
	}
}
