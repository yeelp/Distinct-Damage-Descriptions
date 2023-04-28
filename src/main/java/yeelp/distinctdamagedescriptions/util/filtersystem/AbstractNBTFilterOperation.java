package yeelp.distinctdamagedescriptions.util.filtersystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.nbt.NBTTagString;

public abstract class AbstractNBTFilterOperation<T extends NBTBase> extends AbstractFilterOperation {

	protected enum NBTType {
		END,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		BYTE_ARRAY,
		STRING,
		LIST,
		COMPOUND,
		INT_ARRAY,
		LONG_ARRAY;

		static NBTType getType(NBTBase nbt) {
			return NBTType.values()[nbt.getId()];
		}

		boolean isType(NBTBase nbt) {
			return getType(nbt) == this;
		}
	}

	private final boolean recursive;
	protected final String key;

	protected AbstractNBTFilterOperation(String key, boolean recursive) {
		this.recursive = recursive;
		this.key = key;
	}

	@Override
	public boolean getTestResult(EntityLivingBase entity) {
		NBTTagCompound root = entity.serializeNBT();
		if(root.hasKey(this.key)) {
			if(this.matches(root)) {
				return true;
			}
			if(this.recursive) {
				Collection<NBTTagCompound> compounds = getNestedCompounds(root);
				while(compounds.size() > 0) {
					Collection<NBTTagCompound> subCompounds = new ArrayList<NBTTagCompound>();
					for(NBTTagCompound tag : compounds) {
						if(tag.hasKey(this.key)) {
							if(this.matches(tag)) {
								return true;
							}
							subCompounds.addAll(getNestedCompounds(tag));
						}
					}
					compounds = subCompounds;
				}
			}
		}
		return false;
	}

	protected abstract Optional<T> getTag(NBTTagCompound tag);

	protected abstract boolean matchesValue(T t);

	private final boolean matches(NBTTagCompound tag) {
		return this.getTag(tag).filter(this::matchesValue).isPresent();
	}

	private static Collection<NBTTagCompound> getNestedCompounds(NBTTagCompound tag) {
		return tag.getKeySet().stream().filter((s) -> NBTType.COMPOUND.isType(tag.getTag(s))).map(tag::getCompoundTag).collect(Collectors.toList());
	}

	// protected static

	public static final class HasNBTKeyFilter extends AbstractNBTFilterOperation<NBTBase> {

		protected HasNBTKeyFilter(String key, boolean recursive) {
			super(key, recursive);
		}

		@Override
		protected Optional<NBTBase> getTag(NBTTagCompound tag) {
			return Optional.of(tag.getTag(this.key));
		}

		@Override
		protected boolean matchesValue(NBTBase t) {
			return true;
		}

	}

	public static abstract class HasNBTKeyValueFilter<K extends NBTBase, V> extends AbstractNBTFilterOperation<K> {

		private final V value;

		protected HasNBTKeyValueFilter(String key, boolean recursive, V value) {
			super(key, recursive);
			this.value = value;
		}

		@Override
		protected final Optional<K> getTag(NBTTagCompound tag) {
			return Optional.of(tag.getTag(this.key)).filter((nbt) -> this.getValidTypes().contains(NBTType.getType(nbt))).map(this.getConversionClass()::cast);
		}

		protected final V getValue() {
			return this.value;
		}

		protected final boolean matchesWithCast(NBTBase nbt) {
			if(this.getValidTypes().contains(NBTType.getType(nbt))) {
				return this.matchesValue(this.getConversionClass().cast(nbt));
			}
			return false;
		}

		protected abstract Set<NBTType> getValidTypes();

		protected abstract Class<K> getConversionClass();

	}

	public static final class HasNBTNumberFilter extends HasNBTKeyValueFilter<NBTPrimitive, Long> {

		protected HasNBTNumberFilter(String key, boolean recursive, long value) {
			super(key, recursive, value);
		}

		@Override
		protected boolean matchesValue(NBTPrimitive t) {
			return t.getLong() == this.getValue().longValue();
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.BYTE, NBTType.INT, NBTType.SHORT, NBTType.LONG);
		}

		@Override
		protected Class<NBTPrimitive> getConversionClass() {
			return NBTPrimitive.class;
		}

	}

	public static final class hasNBTBooleanFilter extends HasNBTKeyValueFilter<NBTTagByte, Boolean> {

		protected hasNBTBooleanFilter(String key, boolean recursive, boolean value) {
			super(key, recursive, value);
		}

		@Override
		protected boolean matchesValue(NBTTagByte t) {
			return this.getValue().booleanValue() == (t.getByte() != 0);
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.BYTE);
		}

		@Override
		protected Class<NBTTagByte> getConversionClass() {
			return NBTTagByte.class;
		}

	}

	public static final class HasNBTDecimalFilter extends HasNBTKeyValueFilter<NBTPrimitive, Double> {

		protected HasNBTDecimalFilter(String key, boolean recursive, Double value) {
			super(key, recursive, value);
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.FLOAT, NBTType.DOUBLE);
		}

		@Override
		protected Class<NBTPrimitive> getConversionClass() {
			return NBTPrimitive.class;
		}

		@Override
		protected boolean matchesValue(NBTPrimitive t) {
			return this.getValue().doubleValue() == t.getDouble();
		}

	}

	public static final class HasNBTStringFilter extends HasNBTKeyValueFilter<NBTTagString, String> {

		protected HasNBTStringFilter(String key, boolean recursive, String value) {
			super(key, recursive, value);
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.STRING);
		}

		@Override
		protected Class<NBTTagString> getConversionClass() {
			return NBTTagString.class;
		}

		@Override
		protected boolean matchesValue(NBTTagString t) {
			return t.getString().equals(this.getValue());
		}

	}

	public static abstract class HasNBTCollectionFilter<K extends NBTBase, V> extends HasNBTKeyValueFilter<K, List<V>> {

		private final boolean exact;

		protected HasNBTCollectionFilter(String key, boolean recursive, List<V> value, boolean exact) {
			super(key, recursive, value);
			this.exact = exact;
		}

		@Override
		protected final boolean matchesValue(K t) {
			Iterable<V> it = this.getIterable(t), itVal = this.getIterableFromValue();
			return this.contains(itVal, it) && (!this.exact || this.contains(it, itVal));
		}

		protected abstract Iterable<V> getIterable(K k);

		protected abstract Iterable<V> getIterableFromValue();

		protected final <T extends Iterable<V>, U extends Iterable<V>> boolean contains(T t, U u) {
			Map<V, Integer> map = Maps.newHashMap();
			u.forEach((k) -> map.merge(k, 1, Integer::sum));
			t.forEach((k) -> map.merge(k, -1, Integer::sum));
			return map.values().stream().allMatch((i) -> i >= 0);
		}
	}

	public static final class HasNBTListFilter extends HasNBTCollectionFilter<NBTTagList, NBTBase> {

		protected HasNBTListFilter(String key, boolean recursive, List<NBTBase> value, boolean exact) {
			super(key, recursive, value, exact);
		}

		@Override
		protected Iterable<NBTBase> getIterable(NBTTagList k) {
			return k;
		}

		@Override
		protected Iterable<NBTBase> getIterableFromValue() {
			return this.getValue();
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.LIST);
		}

		@Override
		protected Class<NBTTagList> getConversionClass() {
			return NBTTagList.class;
		}

	}

	public static final class HasNBTArrayFilter extends HasNBTCollectionFilter<NBTBase, Number> {

		protected HasNBTArrayFilter(String key, boolean recursive, List<Number> value, boolean exact) {
			super(key, recursive, value, exact);
		}

		@Override
		protected Iterable<Number> getIterable(NBTBase k) {
			List<Number> lst = Lists.newArrayList();
			if(k instanceof NBTTagByteArray) {
				NBTTagByteArray arr = (NBTTagByteArray) k;
				for(byte b : arr.getByteArray()) {
					lst.add(b);
				}
			}
			else if(k instanceof NBTTagIntArray) {
				NBTTagIntArray arr = (NBTTagIntArray) k;
				for(int i : arr.getIntArray()) {
					lst.add(i);
				}
			}
			else if(k instanceof NBTTagLongArray) {
				NBTTagLongArray arr = (NBTTagLongArray) k;
				for(char c : arr.toString().toCharArray()) {
					if('0' <= c && c <= '9') {
						lst.add(Character.digit(c, 10));
					}
				}
			}
			return lst;
		}

		@Override
		protected Iterable<Number> getIterableFromValue() {
			return this.getValue();
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.BYTE_ARRAY, NBTType.INT_ARRAY, NBTType.LONG_ARRAY);
		}

		@Override
		protected Class<NBTBase> getConversionClass() {
			return NBTBase.class;
		}

	}

	public static final class HasNBTCompoundFilter extends HasNBTKeyValueFilter<NBTTagCompound, NBTTagCompound> {

		private static final class HasNBTListOrArrayFilter extends HasNBTCollectionFilter<NBTBase, NBTBase> {

			private boolean canBeArray = false;

			protected HasNBTListOrArrayFilter(String key, List<NBTBase> value) {
				super(key, false, value, false);
				if(this.getValue().stream().allMatch(NBTType.LONG::isType)) {
					this.canBeArray = true;
				}
			}

			@Override
			protected Iterable<NBTBase> getIterable(NBTBase k) {
				if(k instanceof NBTTagList) {
					return (NBTTagList) k;
				}
				else if(this.canBeArray) {
					List<NBTBase> lst = Lists.newArrayList();
					for(char c : k.toString().toCharArray()) {
						if('0' <= c && c <= '9') {
							lst.add(new NBTTagLong(Character.digit(c, 10)));
						}
					}
					return lst;
				}
				return Lists.newArrayList();
			}

			@Override
			protected Iterable<NBTBase> getIterableFromValue() {
				return this.getValue();
			}

			@Override
			protected Set<NBTType> getValidTypes() {
				Set<NBTType> types = EnumSet.of(NBTType.LIST);
				if(this.canBeArray) {
					types.add(NBTType.BYTE_ARRAY);
					types.add(NBTType.INT_ARRAY);
					types.add(NBTType.LONG_ARRAY);
				}
				return types;
			}

			@Override
			protected Class<NBTBase> getConversionClass() {
				return NBTBase.class;
			}

		}

		private Map<String, HasNBTKeyValueFilter<? extends NBTBase, ?>> nestedFilters;

		protected HasNBTCompoundFilter(String key, boolean recursive, NBTTagCompound value) {
			super(key, recursive, value);
			this.nestedFilters = Maps.newHashMap();
			for(String s : value.getKeySet()) {
				// We create the value tag from JSON and we only use certain tag types for
				// simplicity.
				switch(NBTType.getType(value.getTag(s))) {
					case LONG:
						this.nestedFilters.put(s, new HasNBTNumberFilter(s, false, value.getLong(s)));
						break;
					case DOUBLE:
						this.nestedFilters.put(s, new HasNBTDecimalFilter(s, false, value.getDouble(s)));
						break;
					case STRING:
						this.nestedFilters.put(s, new HasNBTStringFilter(s, false, value.getString(s)));
						break;
					case LIST:
						List<NBTBase> lst = Lists.newArrayList();
						for(NBTBase nbt : (NBTTagList) value.getTag(s)) {
							lst.add(nbt);
						}
						this.nestedFilters.put(s, new HasNBTListOrArrayFilter(s, lst));
						break;
					case COMPOUND:
						this.nestedFilters.put(s, new HasNBTCompoundFilter(s, false, value.getCompoundTag(s)));
						break;
					default:
						break;
				}
			}
		}

		@Override
		protected Set<NBTType> getValidTypes() {
			return EnumSet.of(NBTType.COMPOUND);
		}

		@Override
		protected Class<NBTTagCompound> getConversionClass() {
			return NBTTagCompound.class;
		}

		@Override
		protected boolean matchesValue(NBTTagCompound t) {
			for(String s : this.getValue().getKeySet()) {
				if(!t.hasKey(s)) {
					return false;
				}
				NBTBase nbt = t.getTag(s);
				HasNBTKeyValueFilter<? extends NBTBase, ?> filter = this.nestedFilters.get(s);
				if(!filter.matchesWithCast(nbt)) {
					return false;
				}
			}
			return true;
		}

	}
	
}
