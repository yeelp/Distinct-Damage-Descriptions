package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.integration.client.AbstractModCompatIconAggregator;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatIconAggregator;
import yeelp.distinctdamagedescriptions.integration.tic.capability.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerToolDistribution;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;

public class TinkerToolDamageFormatter extends AbstractTinkersFormatter {

	private static TinkerToolDamageFormatter instance;
	
	private static final IModCompatIconAggregator ICON_AGGREGATOR = makeIconAggregator(getInstance(), TinkerToolDistribution.Tool.cap);
	
	public TinkerToolDamageFormatter() {
		super(KeyTooltip.SHIFT, TinkerToolDistribution.Tool.cap, "damagedistribution");
	}
	
	protected <D extends IDistribution, C extends AbstractTinkersDistribution<D, ?>> TinkerToolDamageFormatter(KeyTooltip keyTooltip, Capability<C> cap, String typeTextKey) {
		super(keyTooltip, cap, typeTextKey);
	}

	@Override
	public boolean applicable(ItemStack t) {
		return isTinkerTool(t);
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.DAMAGE;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return true;
	}

	@Override
	public boolean supportsDamageFormat(DDDDamageFormatter f) {
		return true;
	}

	@Override
	protected List<String> getDefaultTooltips(ItemStack t) {
		IDamageDistribution base = DDDConfigurations.items.getOrFallbackToDefault(YResources.getRegistryString(t));
		return super.parseMapIntoTooltip(base.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(0.0f, base::getWeight)));
	}

	@Override
	protected TooltipTypeFormatter getFormatter() {
		return TooltipTypeFormatter.DEFAULT_DAMAGE;
	}

	public static TinkerToolDamageFormatter getInstance() {
		return instance == null ? instance = new TinkerToolDamageFormatter() : instance;
	}

	@Override
	public IModCompatIconAggregator getIconAggregator() {
		return ICON_AGGREGATOR;
	}
	
	protected static final <C extends IDistributionRequiresUpdate> IModCompatIconAggregator makeIconAggregator(TinkerToolDamageFormatter formatter, Capability<C> cap) {
		return new AbstractModCompatIconAggregator<IDistributionRequiresUpdate>(formatter, cap) {

			@Override
			protected Map<DDDDamageType, Float> getDefaultDistribution(ItemStack stack) {
				IDamageDistribution base = DDDConfigurations.items.getOrFallbackToDefault(YResources.getRegistryString(stack));
				return base.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(0.0f, base::getWeight));
			}
		};
	}
}
