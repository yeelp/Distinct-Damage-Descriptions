package yeelp.distinctdamagedescriptions.registries.impl;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.registries.IDDDRegistry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public abstract class DDDBaseRegistry<T> implements IDDDRegistry<T> {
	protected final Map<String, T> map = new HashMap<>();
	private final Function<T, String> keyFunc;
	private final String name;

	protected DDDBaseRegistry(Function<T, String> f, String name) {
		this.keyFunc = f;
		this.name = name;
		this.init();
	}

	@Override
	public void register(boolean suppressOutput, T obj) {
		if(this.isRegistered(obj)) {
			throw new RuntimeException(obj.toString() + "was already registered!");
		}
		String key = this.keyFunc.apply(obj);
		this.map.put(key, obj);
		if(!suppressOutput) {
			DistinctDamageDescriptions.info(String.format("Registering %s: %s", this.name, this.getRegistrationInfo(key, obj)));
		}
	}
	
	protected abstract String getRegistrationInfo(String regName, T regObj);
	
	@Override
	@SuppressWarnings("unchecked")
	public final void registerAll(boolean suppressOutput, T... objs) {
		for(T t : objs) {
			this.register(suppressOutput, t);
		}
	}

	@Override
	public final T get(String key) {
		return this.map.get(key);
	}

	@Override
	public final Collection<T> getAll() {
		return this.map.values();
	}

	@Override
	public final boolean isRegistered(T obj) {
		return this.map.containsKey(this.keyFunc.apply(obj));
	}

	@Nonnull
	@Override
	public final Iterator<T> iterator() {
		return this.map.values().iterator();
	}
}
