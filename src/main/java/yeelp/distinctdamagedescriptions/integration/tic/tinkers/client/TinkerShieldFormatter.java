package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.List;
import java.util.Map;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatIconAggregator;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatIconAggregator;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerToolDistribution;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;

public final class TinkerShieldFormatter extends AbstractTinkersFormatter {

	private static TinkerShieldFormatter instance;
	
	private static final IModCompatIconAggregator ICON_AGGREGATOR = new AbstractModCompatIconAggregator<IDistributionRequiresUpdate>(getInstance(), TinkerToolDistribution.Shield.cap) {

		@Override
		protected Map<DDDDamageType, Float> getDefaultDistribution(ItemStack stack) {
			return new DDDBaseMap<Float>(0.0f);
		}
	};
	
	protected TinkerShieldFormatter() {
		super(KeyTooltip.CTRL, TinkerToolDistribution.Shield.cap, "shielddist");
	}

	@Override
	public boolean applicable(ItemStack t) {
		return isTinkerTool(t) && t.getItemUseAction() == EnumAction.BLOCK;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f == DDDNumberFormatter.PERCENT;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.SHIELD;
	}

	@Override
	protected List<String> getDefaultTooltips(ItemStack t) {
		return super.parseMapIntoTooltip(new DDDBaseMap<Float>(0.0f));
	}

	@Override
	protected TooltipTypeFormatter getFormatter() {
		return TooltipTypeFormatter.SHIELD;
	}

	@Override
	public IModCompatIconAggregator getIconAggregator() {
		return ICON_AGGREGATOR;
	}
	
	public static TinkerShieldFormatter getInstance() {
		return instance == null ? instance = new TinkerShieldFormatter() : instance;
	}
}
