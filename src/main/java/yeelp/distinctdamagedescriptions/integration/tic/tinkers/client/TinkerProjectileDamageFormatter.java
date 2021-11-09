package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatIconAggregator;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerToolDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;

public final class TinkerProjectileDamageFormatter extends TinkerToolDamageFormatter {
	
	private static TinkerProjectileDamageFormatter instance;
	
	private static final IModCompatIconAggregator ICON_AGGREGATOR = makeIconAggregator(getInstance(), TinkerToolDistribution.Tool.cap);
	
	public TinkerProjectileDamageFormatter() {
		super(KeyTooltip.CTRL, TinkerToolDistribution.Tool.cap, "projectiledistribution");
	}

	@Override
	public boolean applicable(ItemStack t) {
		return t.getItem() instanceof ProjectileCore;
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.PROJECTILE;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f == DDDNumberFormatter.PERCENT;
	}
	
	@Override
	public IModCompatIconAggregator getIconAggregator() {
		return ICON_AGGREGATOR;
	}

	public static TinkerProjectileDamageFormatter getInstance() {
		return instance == null ? instance = new TinkerProjectileDamageFormatter() : instance;
	}
}
