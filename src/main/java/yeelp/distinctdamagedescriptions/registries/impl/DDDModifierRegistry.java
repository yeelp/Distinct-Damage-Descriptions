package yeelp.distinctdamagedescriptions.registries.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.registries.IDDDModifierRegistries.IDDDModifierRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class DDDModifierRegistry<T extends ICapabilityProvider, D extends DDDCapabilityBase<?>, M extends IDDDCapModifier<T>> extends DDDSourcedRegistry<M> implements IDDDModifierRegistry<T, D, M> {

	protected final Supplier<Float> defaultVal;
	protected DDDModifierRegistry(Supplier<Float> defaultVal, String type) {
		super(IDDDCapModifier::getName, "Modifiers: "+type);
		this.defaultVal = defaultVal;
	}

	@Override
	public void init() {
		//nothing
	}

	/**
	 * Get a pool of applicable modifiers, sorted by priority and same priority
	 * modifiers being combined.
	 * 
	 * @param provider the object being modified.
	 * @return an Iterable of applicable modifiers.
	 */
	protected Iterable<M> poolApplicableModifiers(T provider) {
		Map<Integer, M> modifiersNoReallocate = Maps.newHashMap();
		Map<Integer, M> modifiersReallocate = Maps.newHashMap();
		this.map.values().stream().filter((modifier) -> modifier.applicable(provider)).forEach((modifier) -> (modifier.shouldReallocate() ? modifiersReallocate : modifiersNoReallocate).merge(modifier.priority(), this.getNewModifierAccumulator(modifier), this::combine));
		List<M> lst;
		(lst = Lists.newArrayList(modifiersNoReallocate.values())).addAll(modifiersReallocate.values());
		lst.sort(Comparator.naturalOrder());
		return lst;
	}
	
	protected abstract M getNewModifierAccumulator(M modifier);
	
	protected abstract M combine(M m1, M m2);
	
	@Override
	public Set<String> getNamesOfApplicableModifiers(T provider) {
		return this.map.values().stream().filter((modifier) -> modifier.applicable(provider)).map(IDDDCapModifier::getName).collect(Collectors.toSet());
	}
}
