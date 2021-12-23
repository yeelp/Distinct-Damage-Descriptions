package yeelp.distinctdamagedescriptions.util;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.base.Functions;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public class DDDBaseMap<T> extends NonNullMap<DDDDamageType, T> {

	private static final long serialVersionUID = -7187725991607204803L;
	private static final String KEY = "type";
	private static final String VALUE = "weight";

	public DDDBaseMap(Supplier<T> defaultVal) {
		super(defaultVal);
		// TODO Auto-generated constructor stub
	}
	
	public static DDDBaseMap<Float> fromNBT(NBTTagList lst, float defaultVal) {
		DDDBaseMap<Float> map = new DDDBaseMap<Float>(() -> defaultVal);
		lst.forEach((nbt) -> {
			if(nbt instanceof NBTTagCompound) {
				NBTTagCompound tag = (NBTTagCompound) nbt;
				map.put(DDDRegistries.damageTypes.get(tag.getString(KEY)), tag.getFloat(VALUE));
			}
		});
		return map;
	}
	
	public static NBTTagList toNBT(Map<DDDDamageType, Float> map, Supplier<Float> nullMapper) {
		NBTTagList lst = new NBTTagList();
		map.forEach((t, f) -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString(KEY, t.getTypeName());
			tag.setFloat(VALUE, f == null ? nullMapper.get() : f);
			lst.appendTag(tag);
		});
		return lst;
	}
	
	public static NBTTagList toNBT(Map<DDDDamageType, Float> map) {
		return toNBT(map, () -> 0.0f);
	}
	
	/**
	 * Creates a Collector that collects damage types into a DDDBaseMap
	 * @param <U> the types of values in the map
	 * @param defaultVal the default value of the map
	 * @param valueMapper the mapper that maps damage types to map values
	 * @return The Collector
	 */
	public static <U> Collector<DDDDamageType, ?, DDDBaseMap<U>> typesToDDDBaseMap(Supplier<U> defaultVal, Function<DDDDamageType, ? extends U> valueMapper) {
		return Collectors.toMap(Functions.identity(), valueMapper, (BinaryOperator<U>)(u1, u2) -> { 
			throw new IllegalStateException("Can't collect on duplicate keys"); 
		}, () -> new DDDBaseMap<U>(defaultVal));
	}
}
