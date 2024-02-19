package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.function.Function;

public abstract class DDDUnsourcedRegistry<T> extends DDDBaseRegistry<T> {

	protected DDDUnsourcedRegistry(Function<T, String> f, String name) {
		super(f, name);
	}

	@Override
	protected String getRegistrationInfo(String regName, Object regObj) {
		return regName;
	}

}
