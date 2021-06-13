package yeelp.distinctdamagedescriptions.util.tooltipsystem;

/**
 * The singleton formatter for Hwyla tooltip formatting.
 * @author Yeelp
 *
 */
public class HwylaTooltipFormatter extends MobResistancesFormatter {
	
	private HwylaTooltipFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.STANDARD);
	}

	/**
	 * Return the singleton Hwyla formatter if it exists, returning a new instance if it doesn't
	 * @return the Hwyla formatter or a new one if it doesn't exist.
	 */
	public static MobResistancesFormatter getInstance() {
		return instance == null ? new HwylaTooltipFormatter() : instance;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return f == DDDDamageFormatter.STANDARD;
	}
}
