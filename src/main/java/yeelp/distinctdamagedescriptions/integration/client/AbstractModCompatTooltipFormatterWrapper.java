package yeelp.distinctdamagedescriptions.integration.client;

import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;

import java.util.List;

public abstract class AbstractModCompatTooltipFormatterWrapper<T> implements IModCompatTooltipFormatter<T> {

	private final TooltipFormatter<T> formatter;
	private final IconAggregator iconAggregator;
	
	public AbstractModCompatTooltipFormatterWrapper(TooltipFormatter<T> formatter, IconAggregator iconAggregator) {
		this.formatter = formatter;
		this.iconAggregator = iconAggregator;
	}
	
	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return this.formatter.supportsDamageFormat(f);
	}

	@Override
	public void setDamageFormatter(DDDDamageFormatter f) {
		//nothing to set, just uses wrapped one.
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return this.formatter.getNumberFormattingStrategy();
	}

	@Override
	public List<String> format(T t) {
		return this.formatter.format(t);
	}

	@Override
	public TooltipOrder getType() {
		return this.formatter.getType();
	}

	@Override
	public IconAggregator getIconAggregator() {
		return this.iconAggregator;
	}

}
