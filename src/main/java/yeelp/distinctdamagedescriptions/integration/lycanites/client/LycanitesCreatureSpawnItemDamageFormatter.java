package yeelp.distinctdamagedescriptions.integration.lycanites.client;

import com.lycanitesmobs.core.item.ItemCustomSpawnEgg;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobDamageDistributionIconAggregator;

public final class LycanitesCreatureSpawnItemDamageFormatter extends MobDamageDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static LycanitesCreatureSpawnItemDamageFormatter instance;

	private LycanitesCreatureSpawnItemDamageFormatter() {
		super(LycanitesFormatterUtilities::getCreatureResourceLocation);
	}

	@Override
	public IconAggregator getIconAggregator() {
		return LycanitesDamageIconAggregator.getInstance();
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ItemCustomSpawnEgg;
	}

    public static LycanitesCreatureSpawnItemDamageFormatter getInstance() {
		return instance == null ? instance = new LycanitesCreatureSpawnItemDamageFormatter() : instance;
	}

	protected static final class LycanitesDamageIconAggregator extends MobDamageDistributionIconAggregator {

		private LycanitesDamageIconAggregator() {
			super(LycanitesCreatureSpawnItemDamageFormatter.getInstance(), LycanitesFormatterUtilities::getCreatureResourceLocation);
		}

		private static LycanitesDamageIconAggregator instance;

		public static LycanitesCreatureSpawnItemDamageFormatter.LycanitesDamageIconAggregator getInstance() {
			return instance == null ? instance = new LycanitesCreatureSpawnItemDamageFormatter.LycanitesDamageIconAggregator() : instance;
		}
	}

}
