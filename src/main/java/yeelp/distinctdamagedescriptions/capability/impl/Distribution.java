package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public abstract class Distribution extends AbstractUpdatableCapability<NBTTagCompound> implements IDistribution {
	protected DDDBaseMap<Float> distMap = new DDDBaseMap<Float>(() -> 0.0f);
	protected DDDBaseMap<Float> originalMap = new DDDBaseMap<Float>(() -> 0.0f);
	protected static final String ORIGINAL_WEIGHTS = "originalWeights";
	protected static final String MODDED_WEIGHTS = "moddedWeights";

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
		this.originalMap.putAll(this.distMap);
	}

	protected Distribution(Map<DDDDamageType, Float> weightMap) {
		this.setNewMap(this.distMap, weightMap);
		this.originalMap.putAll(this.distMap);
	}

	@Override
	public NBTTagCompound serializeSpecificNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(ORIGINAL_WEIGHTS, DDDBaseMap.toNBT(this.originalMap));
		tag.setTag(MODDED_WEIGHTS, DDDBaseMap.toNBT(this.distMap));
		return tag;
	}

	@Override
	public void deserializeSpecificNBT(NBTTagCompound tag) {
		this.distMap = DDDBaseMap.fromNBT(tag.getTagList(MODDED_WEIGHTS, NBT.COMPOUND_TAG_ID), () -> 0.0f);
		this.originalMap = DDDBaseMap.fromNBT(tag.getTagList(ORIGINAL_WEIGHTS, NBT.COMPOUND_TAG_ID), () -> 0.0f);
		if(this.originalMap.isEmpty() && !this.distMap.isEmpty()) {
			this.originalMap.putAll(this.distMap);
		}
		else if(this.distMap.isEmpty() && !this.originalMap.isEmpty()) {
			this.distMap.putAll(this.originalMap);
		}
		else if(this.distMap.isEmpty() && this.originalMap.isEmpty() && !this.canHaveEmptyDistribution()) {
			DistinctDamageDescriptions.err("NBT for distribution was empty and should not be! Reverting to Bludgeoning!");
			Stream.of(this.distMap, this.originalMap).forEach((m) -> m.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f));
		}
		if(!this.areWeightsValid()) {
			DistinctDamageDescriptions.err(String.format("Weights: %s are invalid! Scrapping!", this.toString()));
			if(this.canHaveEmptyDistribution()) {
				this.distMap.clear();
				this.originalMap.clear();
			}
			else {
				Stream.of(this.distMap, this.originalMap).forEach((m) -> m.put(DDDBuiltInDamageType.BLUDGEONING, 1.0f));
			}
		}
	}
	
	@Override
	public void deserializeOldNBT(NBTBase nbt) {
		if(nbt instanceof NBTTagList) {
			this.distMap = DDDBaseMap.fromNBT((NBTTagList) nbt, () -> 0.0f);
			this.originalMap.clear();
			this.originalMap.putAll(this.distMap);
		}
	}

	@Override
	public float getWeight(DDDDamageType type) {
		return this.distMap.get(type);
	}
	
	@Override
	public float getBaseWeight(DDDDamageType type) {
		return this.originalMap.get(type);
	}

	@Override
	public void setWeight(DDDDamageType type, float amount) {
		this.setWeight(this.distMap, type, amount);
	}
	
	@Override
	public void setBaseWeight(DDDDamageType type, float amount) {
		this.setWeight(this.originalMap, type, amount);
	}

	@Override
	public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		this.setNewMap(this.distMap, map);
	}
	
	@Override
	public void setNewBaseWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
		this.setNewMap(this.originalMap, map);
	}

	@Override
	public Set<DDDDamageType> getCategories() {
		return this.getTypes(this.distMap);
	}
	
	@Override
	public Set<DDDDamageType> getBaseCategories() {
		return this.getTypes(this.originalMap);
	}

	<R, T extends DDDBaseMap<R>> T distribute(T map, Function<Float, R> valueFunc) {
		for(Entry<DDDDamageType, Float> entry : this.distMap.entrySet()) {
			map.put(entry.getKey(), valueFunc.apply(entry.getValue()));
		}
		return map;
	}

	private final void setNewMap(DDDBaseMap<Float> target, Map<DDDDamageType, Float> map) throws InvariantViolationException {
		if(invariantViolated(map.values())) {
			throw new InvariantViolationException("Some weights are non positive!");			
		}
		target.clear();
		map.entrySet().stream().filter((e) -> {
			float val = e.getValue();
			return val > 0 || (val == 0.0f && this.allowZeroWeightedEntries());
		}).forEach((e) -> target.put(e.getKey(), e.getValue()));
	}

	protected final DDDBaseMap<Float> copyMap(float defaultVal) {
		return this.distMap.entrySet().stream().collect(() -> new DDDBaseMap<Float>(() -> defaultVal), (m, e) -> m.put(e.getKey(), e.getValue()), DDDBaseMap::putAll);
	}

	protected static final <Dist extends Distribution & IDistribution> DDDBaseMap<Float> copyMap(Dist dist) {
		return dist.copyMap(dist.distMap.getDefaultValue());
	}
	
	protected abstract boolean allowZeroWeightedEntries();
	
	protected abstract boolean canHaveEmptyDistribution();
	
	protected void updateOriginalWeightsToCurrentWeights() {
		this.originalMap.clear();
		this.originalMap.putAll(this.distMap);
	}
	
	protected boolean areWeightsValid() {
		return !invariantViolated(this.distMap.values()) && !invariantViolated(this.originalMap.values());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		builder.append(YLib.joinNiceString(true, ",", this.distMap.entrySet().stream().map((e) -> String.format("(%s, %.2f)", e.getKey().getTypeName(), e.getValue())).toArray(String[]::new)));
		return builder.append("]").toString();
	}
	
	private void setWeight(DDDBaseMap<Float> map, DDDDamageType type, float amount) {
		if(amount < 0) {
			throw new InvariantViolationException("Can't set negative weight!");
		}
		if(!this.allowZeroWeightedEntries() && amount == 0.0f) {
			throw new InvariantViolationException("This Distribution doesn't support zero weighted entries!");
		}
		map.put(type, amount);
	}
	
	private Set<DDDDamageType> getTypes(DDDBaseMap<Float> map) {
		Set<DDDDamageType> set = Sets.newHashSet(map.keySet());
		set.removeIf((type) -> !type.isUsable() || map.get(type) < 0 || (map.get(type) == 0 && !this.allowZeroWeightedEntries()));
		return set;
	}
}
