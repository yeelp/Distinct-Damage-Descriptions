package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.impl.AbstractDDDCapModifier;

public abstract class CTDDDModifier<T extends ICapabilityProvider> extends AbstractDDDCapModifier<T> {

	private final String name;
	private final int priority;
	protected CTDDDModifier(String name, boolean shouldReallocate, int priority) {
		super(() -> 0.0f, Source.CoT, shouldReallocate);
		this.name = name;
		this.priority = priority;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public int priority() {
		return this.priority;
	}

}
