package yeelp.distinctdamagedescriptions.integration.hwyla.client;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;

public final class HwylaTooltipMaker {
	private static final HwylaMobResistanceFormatter resistances = HwylaMobResistanceFormatter.getInstance();
	private static final HwylaMobDamageFormatter damage = HwylaMobDamageFormatter.getInstance();

	static {
		updateFormatters();
	}

	/**
	 * Make a tooltip addition for HWYLA.
	 * 
	 * @param entity
	 * @return a List with all the HWYLA info for this mob, if applicable, otherwise
	 *         an empty list.
	 */
	public static List<String> makeHwylaTooltipStrings(EntityLivingBase entity) {
		List<String> tip = damage.format(entity);
		tip.addAll(resistances.format(entity));
		return tip;
	}

	public static void updateFormatters() {
		DDDNumberFormatter numFormat = ModConfig.client.showNumberValuesWhenPossible ? DDDNumberFormatter.PLAIN : DDDNumberFormatter.PERCENT;
		if(damage.supportsNumberFormat(numFormat)) {
			damage.setNumberFormatter(numFormat);
		}
	}
}
