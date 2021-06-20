package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ArmorDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.Icon;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobResistanceIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ProjectileDamageDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ShieldDistributionIconAggregator;

/**
 * Collected formatters and icon aggregators into single instance for tooltip creation
 * @author Yeelp
 *
 * @see TooltipFormatter
 * @see IconAggregator
 */
public enum TooltipMaker {
	ITEM(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.items.configured(registryString) || ModConfig.client.alwaysShowDamageDistTooltip;
		}
	},
	PROJECTILE(ProjectileDistributionFormatter.getInstance(), ProjectileDamageDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.projectiles.isProjectilePairRegistered(registryString);
		}
	},
	ARMOR(ArmorDistributionFormatter.getInstance(), ArmorDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.armors.configured(registryString);
		}
	},
	SHIELD(ShieldDistributionFormatter.getInstance(), ShieldDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.shields.configured(registryString);
		}
	},
	MOB_RESISTANCES(MobResistancesFormatter.getInstance(), MobResistanceIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			boolean isMonsterPlacer = stack.getItem() instanceof ItemMonsterPlacer;
			boolean underlyingMobIsConfigured = Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(stack)).map(ResourceLocation::toString).map(DDDConfigurations.mobResists::configured).orElse(false);
			return isMonsterPlacer && underlyingMobIsConfigured;
		}
	},
	HWYLA(HwylaTooltipFormatter.getInstance(), null) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return false;
		}
	};
	
	static {
		updateFormatters();
	}

	private TooltipFormatter formatter;
	private IconAggregator aggregator;
	
	private TooltipMaker(TooltipFormatter formatter, IconAggregator aggregator) {
		this.formatter = formatter;
		this.aggregator = aggregator;
	}
	
	/**
	 * Update formatters
	 */
	public static void updateFormatters() {
		DDDNumberFormatter numFormat = ModConfig.client.showNumberValuesWhenPossible ? DDDNumberFormatter.PLAIN : DDDNumberFormatter.PERCENT;
		DDDDamageFormatter damageFormat = ModConfig.client.useIcons ? DDDDamageFormatter.ICON : DDDDamageFormatter.COLOURED;
		for(TooltipMaker maker : TooltipMaker.values()) {
			if(maker.formatter.supportsNumberFormat(numFormat)) {
				maker.formatter.setNumberFormatter(numFormat);
			}
			if(maker.formatter.supportsDamageFormat(damageFormat)) {
				maker.formatter.setDamageFormatter(damageFormat);
			}
		}
	}
	
	/**
	 * Make a tooltip addition for an ItemStack
	 * @param stack stack to add a tooltip to.
	 * @return A List of Strings with additional information about all the DDD capabilities present on the item, including mob resistances for spawn eggs.
	 */
	public static List<String> makeTooltipStrings(ItemStack stack) {
		return getApplicableMakers(stack).map((m) -> m.formatter).<List<String>>collect(LinkedList<String>::new, (l, f) -> l.addAll(f.format(stack)), List<String>::addAll);
	}
	
	/**
	 * Make a tooltip addition for HWYLA.
	 * 
	 *<p>
	 * @implNote
	 * Assuming a valid Entity is being looked at, this is done by calling {@code HwylaTooltipFormatter.getInstance().format(null)} first. This returns a List containing just the "header info"
	 * (The "Hold &lt;CTRL&gt; for Mob Resistances" part of the tooltip, see {@link AbstractCapabilityTooltipFormatter#format(ItemStack)} for implementation).
	 * Then, {@code HwylaTooltipFormatter.getInstance().formatCapabilityFor(null, cats)}, where {@code cats} is the {@link MobResistanceCategories} for this Entity, is called and the results are appended to
	 * the former call and returned. If no valid Entity is being looked at (They have no mob resistances), an empty list is returned.
	 * @param entity
	 * @return a List with all the HWYLA info for this mob, if applicable, otherwise an empty list.
	 */
	public static List<String> makeHwylaTooltipStrings(Entity entity) {
		Optional<MobResistanceCategories> cats = YResources.getEntityIDString(entity).map(DDDConfigurations.mobResists::get);
		if(cats.isPresent()) {
			List<String> tip = HWYLA.formatter.format(null);
			HwylaTooltipFormatter.getInstance().formatCapabilityFor(null, cats.get()).ifPresent(tip::addAll);
			return tip;
		}
		else {
			return ImmutableList.of();
		}
	}
	
	/**
	 * Get a list of icons to draw for a tooltip
	 * @param stack the ItemStack
	 * @param x the x coord of the event
	 * @param y the y coord of the event
	 * @param tooltips the tooltips on the string
	 * @return a List of icons to draw
	 */
	public static List<Icon> getIconsFor(ItemStack stack, int x, int y, List<String> tooltips) {
		return getApplicableMakers(stack).map((m) -> m.aggregator).<List<Icon>>collect(LinkedList<Icon>::new, (l, a) -> l.addAll(a.getIconsToDraw(stack, x, y, tooltips)), List<Icon>::addAll);
	}
	
	private static Stream<TooltipMaker> getApplicableMakers(ItemStack stack) {
		final String regKey = YResources.getRegistryString(stack);
		return Arrays.stream(TooltipMaker.values()).filter((m) -> m.isApplicable(stack, regKey)).sorted();
	}
	
	protected abstract boolean isApplicable(ItemStack stack, String registryString);
}
