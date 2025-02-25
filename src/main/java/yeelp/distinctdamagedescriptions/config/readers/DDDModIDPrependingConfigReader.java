package yeelp.distinctdamagedescriptions.config.readers;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

public final class DDDModIDPrependingConfigReader<T> extends DDDMultiEntryConfigReader<T> {

	private final DDDMultiEntryConfigReader<T> delegate;
	private final String modid;

	public DDDModIDPrependingConfigReader(String modid, DDDMultiEntryConfigReader<T> internal) {
		super(internal);
		this.modid = modid;
		this.delegate = internal;
	}

	@Override
	protected Tuple<String, T> readEntry(String s) throws DDDConfigReaderException {
		if(!s.split(";")[0].contains(":")) {
			return this.delegate.readEntry(this.modid + ":" + s);
		}
		return this.delegate.readEntry(s);
	}

}
