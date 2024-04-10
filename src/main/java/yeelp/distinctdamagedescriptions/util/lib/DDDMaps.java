package yeelp.distinctdamagedescriptions.util.lib;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.base.Functions;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;

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
	
	public static final class DamageMap extends DDDBaseMap<Float> {
		private static final long serialVersionUID = 1800888433080051037L;

		DamageMap() {
			super(() -> 0.0f);
		}
		
		public static Collector<DDDDamageType, ?, DamageMap> typesToDamageMap(Function<DDDDamageType, Float> valueMapper) {
			return DDDMaps.typesToMap(valueMapper, DDDMaps::newDamageMap);
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
}
