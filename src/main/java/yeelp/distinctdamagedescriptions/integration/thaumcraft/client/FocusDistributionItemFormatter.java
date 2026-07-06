package yeelp.distinctdamagedescriptions.integration.thaumcraft.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.thaumcraft.dist.ThaumcraftFocusDistribution;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.*;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FocusDistributionItemFormatter extends ItemDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {

	private static FocusDistributionItemFormatter instance;
	
	private FocusDistributionItemFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, FocusDistributionItemFormatter::getDist, "thaumcraft.focusdistribution");
	}

	@Override
	public boolean applicable(ItemStack t) {
		return (t.getItem() instanceof ICaster || t.getItem() instanceof ItemFocus) && hasFocusEffects(t);
	}

	@Override
	public IconAggregator getIconAggregator() {
		return FocusDistributionIconAggregator.getInstance();
	}
	
	@Override
	public TooltipOrder getType() {
		//spells are just fancy projectiles half the time.
		return TooltipOrder.PROJECTILE;
	}
	
	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}

    private static boolean hasFocusEffects(ItemStack stack) {
		FocusPackage pack = getPackage(stack);
		if(pack != null) {
			return pack.getFocusEffects().length > 0;
		}
		return false;
	}
	
	private static FocusPackage getPackage(ItemStack stack) {
		Item item = stack.getItem();
		return ItemFocus.getPackage(item instanceof ICaster ? ((ICaster) item).getFocusStack(stack) : stack);
	}
	
	static Optional<IDamageDistribution> getDist(ItemStack stack) {
		return ThaumcraftFocusDistribution.getCombinedDamageDistributionForAspects(Arrays.stream(getPackage(stack).getFocusEffects()).map(FocusEffect::getAspect).collect(Collectors.toSet()));
	}
	
	public static FocusDistributionItemFormatter getInstance() {
		return instance == null ? instance = new FocusDistributionItemFormatter() : instance;
	}
	
	private static final class FocusDistributionIconAggregator extends ItemDamageDistributionIconAggregator {

		private static FocusDistributionIconAggregator instance;
		
		private FocusDistributionIconAggregator() {
			super(FocusDistributionItemFormatter.getInstance(), FocusDistributionItemFormatter::getDist);
		}
		
		public static FocusDistributionIconAggregator getInstance() {
			return instance == null ? instance = new FocusDistributionIconAggregator() : instance;
		}
		
	}

}
