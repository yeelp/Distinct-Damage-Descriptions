package yeelp.distinctdamagedescriptions.integration.client;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.AbstractCapabilityTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter;

public abstract class AbstractModCompatTooltipFormatter extends AbstractCapabilityTooltipFormatter<IDistributionRequiresUpdate, ItemStack> implements IModCompatTooltipFormatter<ItemStack> {

	protected <C extends IDistributionRequiresUpdate> AbstractModCompatTooltipFormatter(KeyTooltip keyTooltip, DDDNumberFormatter numberFormatter, DDDDamageFormatter damageFormatter, Capability<C> cap, String typeTextKey) {
		super(keyTooltip, numberFormatter, damageFormatter, (t) -> t.getCapability(cap, null), typeTextKey);
	}
	
	protected final List<String> parseMapIntoTooltip(Map<DDDDamageType, Float> map) {
		return map.entrySet().stream().sorted(Comparator.<Entry<DDDDamageType, Float>, DDDDamageType>comparing(Entry::getKey).thenComparing(Entry::getValue)).map((e) -> this.getFormatter().format(e.getKey(), e.getValue(), this)).collect(Collectors.toList());
	}
	
	@Override
	protected Optional<List<String>> formatCapabilityFor(ItemStack t, IDistributionRequiresUpdate cap) {
		if(t == null || cap == null) {
			return Optional.empty();
		}
		return Optional.of(cap.getUpdatedDistribution(t).map(this::parseMapIntoTooltip).orElse(this.getDefaultTooltips(t)));
	}

	protected abstract TooltipTypeFormatter getFormatter();
	
	protected abstract List<String> getDefaultTooltips(ItemStack t);
}
