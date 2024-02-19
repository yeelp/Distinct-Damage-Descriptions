package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.function.Function;

import yeelp.distinctdamagedescriptions.api.IHasCreationSource;

public abstract class DDDSourcedRegistry<T extends IHasCreationSource> extends DDDBaseRegistry<T> {

	protected DDDSourcedRegistry(Function<T, String> f, String name) {
		super(f, name);
	}

	@Override
	protected final String getRegistrationInfo(String regName, T regObj) {
		return String.format("%s, (%s)", regName, regObj.getCreationSourceString());
	}

}
