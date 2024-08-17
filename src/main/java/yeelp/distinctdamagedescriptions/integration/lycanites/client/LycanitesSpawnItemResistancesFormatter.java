package yeelp.distinctdamagedescriptions.integration.lycanites.client;

import com.lycanitesmobs.core.item.ItemCustomSpawnEgg;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobResistancesFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobResistanceIconAggregator;

public class LycanitesSpawnItemResistancesFormatter extends MobResistancesFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static LycanitesSpawnItemResistancesFormatter instance;

	protected LycanitesSpawnItemResistancesFormatter() {
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, LycanitesFormatterUtilities::getCreatureResourceLocation);
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ItemCustomSpawnEgg;
	}

	@Override
	public IconAggregator getIconAggregator() {
		return LycanitesResistancesIconAggregator.getInstance();
	}

	public static LycanitesSpawnItemResistancesFormatter getInstance() {
		return instance == null ? instance = new LycanitesSpawnItemResistancesFormatter() : instance;
	}

	private static final class LycanitesResistancesIconAggregator extends MobResistanceIconAggregator {
		private static LycanitesSpawnItemResistancesFormatter.LycanitesResistancesIconAggregator instance;

		private LycanitesResistancesIconAggregator() {
			super(LycanitesSpawnItemResistancesFormatter.getInstance(), LycanitesFormatterUtilities::getCreatureResourceLocation);
		}

		public static LycanitesSpawnItemResistancesFormatter.LycanitesResistancesIconAggregator getInstance() {
			return instance == null ? instance = new LycanitesSpawnItemResistancesFormatter.LycanitesResistancesIconAggregator() : instance;
		}
	}

}
