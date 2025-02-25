package yeelp.distinctdamagedescriptions.config.readers;

import java.util.function.Function;

import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

public final class DDDNumericModIDPrependingConfigReader<T extends Number> extends DDDNumericEntryConfigReader<T> {

	private final String modid;

	public DDDNumericModIDPrependingConfigReader(String modID, String name, String[] configList, IDDDConfiguration<T> config, Function<String, T> factory) {
		super(name, configList, config, factory);
		this.modid = modID;
	}

	@Override
	protected Tuple<String, T> readEntry(String s) throws DDDConfigReaderException {
		if(!s.contains(":")) {
			return super.readEntry(this.modid + ":" + s);
		}
		return super.readEntry(s);
	}
}
