package yeelp.distinctdamagedescriptions.integration.lycanites.client;

import com.lycanitesmobs.core.item.ItemCustomSpawnEgg;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobDamageDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobDamageDistributionIconAggregator;

public final class LycantiesCreatureSpawnItemDamageFormatter extends MobDamageDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static LycantiesCreatureSpawnItemDamageFormatter instance;

	private LycantiesCreatureSpawnItemDamageFormatter() {
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

	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return this.applicable(stack);
	}

	public static LycantiesCreatureSpawnItemDamageFormatter getInstance() {
		return instance == null ? instance = new LycantiesCreatureSpawnItemDamageFormatter() : instance;
	}

	protected static final class LycanitesDamageIconAggregator extends MobDamageDistributionIconAggregator {

		private LycanitesDamageIconAggregator() {
			super(LycantiesCreatureSpawnItemDamageFormatter.getInstance(), LycanitesFormatterUtilities::getCreatureResourceLocation);
		}

		private static LycanitesDamageIconAggregator instance;

		public static LycantiesCreatureSpawnItemDamageFormatter.LycanitesDamageIconAggregator getInstance() {
			return instance == null ? instance = new LycantiesCreatureSpawnItemDamageFormatter.LycanitesDamageIconAggregator() : instance;
		}
	}

}
