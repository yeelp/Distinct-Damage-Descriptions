package yeelp.distinctdamagedescriptions.util.tooltipsystem;

public enum DamageDistributionNumberFormat implements ObjectFormatter<Float> {
	PLAIN(DDDNumberFormatter.PLAIN),
	PERCENT(DDDNumberFormatter.PERCENT);
	
	private final DDDNumberFormatter formatter;
	private DamageDistributionNumberFormat(DDDNumberFormatter formatter) {
		this.formatter = formatter;
	}
	
	@Override
	public String format(Float t) {
		return this.formatter.format(t);
	}
}
