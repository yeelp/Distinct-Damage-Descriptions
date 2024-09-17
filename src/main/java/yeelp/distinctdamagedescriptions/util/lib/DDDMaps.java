package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.Sets;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

public final class DDDMaps {

	private DDDMaps() {
		throw new RuntimeException("DDDMaps not to be instantiated");
	}
	
	public static DamageMap newDamageMap() {
		return new DamageMap();
	}
	
	public static ArmorMap newArmorMap() {
		return new ArmorMap();
	}
	
	public static ResistMap newResistMap() {
		return new ResistMap();
	}
	
	public static void adjustHiddenWeightsToUnknown(DDDBaseMap<Float> map) {
		TypeWeightAccumulator hiddenTypes = map.entrySet().stream().filter((e) -> e.getKey().isHidden()).reduce(new TypeWeightAccumulator(), (twa, e) -> twa.addEntry(e.getKey(), e.getValue()), TypeWeightAccumulator::combine);
		float unknownWeight = hiddenTypes.getWeight();
		hiddenTypes.getTypes().forEach(map::remove);
		if(unknownWeight > 0.0f) {
			map.put(DDDBuiltInDamageType.UNKNOWN, unknownWeight);
		}
	}
	
	public static final class DamageMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 1800888433080051037L;

		DamageMap() {
			super(() -> 0.0f);
		}
		
		public static Collector<DDDDamageType, ?, DamageMap> typesToDamageMap(Function<DDDDamageType, Float> valueMapper) {
			return DDDMaps.typesToMap(valueMapper, DDDMaps::newDamageMap);
		}
		
		public void distributeDamageToCurrentTypes(float dmg) {
			float total = this.values().stream().reduce(0.0f, Float::sum);
			if(Math.abs(total - dmg) >= 0.01 && total != 0) {
				float ratio = dmg / total;
				this.keySet().forEach((k) -> this.computeIfPresent(k, (key, val) -> val * ratio));
			}
		}
	}
	
	public static final class ArmorMap extends DDDBaseMap<ArmorValues> {
		private static final long serialVersionUID = -7103973048532333006L;

		ArmorMap() {
			super(() -> new ArmorValues());
		}
		
		public static Collector<DDDDamageType, ?, ArmorMap> typesToArmorMap(Function<DDDDamageType, ArmorValues> valueMapper) {
			return DDDMaps.typesToMap(valueMapper, DDDMaps::newArmorMap);
		}
	}
	
	public static final class ResistMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 7527157629195618664L;

		ResistMap() {
			super(() -> 0.0f);
		}
		
		public static Collector<DDDDamageType, ?, ResistMap> typesToResistMap(Function<DDDDamageType, Float> valueMapper) {
			return DDDMaps.typesToMap(valueMapper, DDDMaps::newResistMap);
		}
	}
	
	static <U, M extends DDDBaseMap<U>> Collector<DDDDamageType, ?, M> typesToMap(Function<DDDDamageType, U> valueMapper, Supplier<M> mapSup) {
		return Collectors.toMap(Functions.identity(), valueMapper, (BinaryOperator<U>) (u1, u2) -> {
			throw new IllegalArgumentException("Can't collect on duplicate keys!");
		}, mapSup);
	}
	
	private static final class TypeWeightAccumulator {

		private final Set<DDDDamageType> types = Sets.newHashSet();
		private float weight = 0.0f;
		
		public TypeWeightAccumulator() {
			//no-op
		}

		TypeWeightAccumulator addEntry(DDDDamageType type, float weight) {
			this.types.add(type);
			this.weight += weight;
			return this;
		}
		
		Set<DDDDamageType> getTypes() {
			return this.types;
		}
		
		float getWeight() {
			return this.weight;
		}
		
		TypeWeightAccumulator combine(TypeWeightAccumulator twa) {
			this.types.addAll(twa.getTypes());
			this.weight += twa.getWeight();
			return this;
		}
	}
}
