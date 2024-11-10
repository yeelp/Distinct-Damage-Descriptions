package yeelp.distinctdamagedescriptions.api.impl;

import java.util.function.Supplier;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

/**
 * A skeletal implementation of an {@link IDDDCapModifier}.
 * @author Yeelp
 *
 */
public abstract class AbstractDDDCapModifier<T extends ICapabilityProvider> extends DDDBaseMap<Float> implements IDDDCapModifier<T> {

	private Source src;
	private final boolean shouldReallocate;
	
	protected AbstractDDDCapModifier(Supplier<Float> defaultVal, Source src, boolean shouldReallocate) {
		super(defaultVal);
		this.src = src;
		this.shouldReallocate = shouldReallocate;
	}

	@Override
	public Source getCreationSource() {
		return this.src;
	}
	
	@Override
	public boolean shouldReallocate() {
		return this.shouldReallocate;
	}
}
