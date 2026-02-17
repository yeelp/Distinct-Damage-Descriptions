package yeelp.distinctdamagedescriptions.integration.client;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.integration.capability.ModUpdatingDamageDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class UpdatingDamageDistributionTooltipFormatter extends AbstractModCompatTooltipFormatterWrapper<ItemStack> {

	private static UpdatingDamageDistributionTooltipFormatter instance;
	private static boolean registered = false;
	private static Set<Capability<? extends ModUpdatingDamageDistribution>> caps = Sets.newHashSet();
	
	private UpdatingDamageDistributionTooltipFormatter() {
		super(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance());
	}
	
	@Override
	public boolean applicable(ItemStack t) {
		return caps.stream().anyMatch((c) -> t.hasCapability(c, null));
	}


	public static void registerModCompatTooltipFormatting(Capability<? extends ModUpdatingDamageDistribution> cap) {
		if(!registered) {
			TooltipDistributor.registerModCompat(getInstance());
			registered = true;
		}
		caps.add(cap);
	}
	
	public static UpdatingDamageDistributionTooltipFormatter getInstance() {
		return instance == null ? instance = new UpdatingDamageDistributionTooltipFormatter() : instance;
	}
}
