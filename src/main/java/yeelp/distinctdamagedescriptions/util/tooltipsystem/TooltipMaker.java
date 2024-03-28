package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ArmorDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.Icon;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobDamageDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.MobResistanceIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ProjectileDamageDistributionIconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ShieldDistributionIconAggregator;

/**
 * Collected formatters and icon aggregators into single instance for tooltip
 * creation
 * 
 * @author Yeelp
 *
 * @see TooltipFormatter
 * @see IconAggregator
 */
public enum TooltipMaker {
	ITEM(ItemDistributionFormatter.getInstance(), ItemDamageDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.items.configured(registryString) || (!MOB_DAMAGE.isApplicable(stack, registryString) && ModConfig.client.alwaysShowDamageDistTooltip);
		}
	},
	MOB_DAMAGE(MobDamageDistributionFormatter.getInstance(), MobDamageDistributionIconAggregator.getInstance()) {
		@Override
		protected boolean isApplicable(ItemStack stack, String registryString) {
			return ModConfig.client.showMobDamage && stack.getItem() instanceof ItemMonsterPlacer;
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
	};

	static {
		updateFormatters();
	}

	private TooltipFormatter<ItemStack> formatter;
	private IconAggregator aggregator;

	private TooltipMaker() {
		this(null, null);
	}

	private TooltipMaker(TooltipFormatter<ItemStack> formatter, IconAggregator aggregator) {
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
	 * 
	 * @param stack stack to add a tooltip to.
	 * @return A List of Strings with additional information about all the DDD
	 *         capabilities present on the item, including mob resistances for spawn
	 *         eggs.
	 */
	public static List<String> makeTooltipStrings(ItemStack stack) {
		return getApplicableMakers(stack).map((m) -> m.formatter).<List<String>>collect(LinkedList<String>::new, (l, f) -> l.addAll(f.format(stack)), List<String>::addAll);
	}

	/**
	 * Get a list of icons to draw for a tooltip
	 * 
	 * @param stack    the ItemStack
	 * @param x        the x coord of the event
	 * @param y        the y coord of the event
	 * @param tooltips the tooltips on the string
	 * @return a List of icons to draw
	 */
	public static List<Icon> getIconsFor(ItemStack stack, int x, int y, List<String> tooltips) {
		return getApplicableMakers(stack).map((m) -> m.aggregator).<List<Icon>>collect(LinkedList<Icon>::new, (l, a) -> l.addAll(a.getIconsToDraw(stack, x, y, tooltips)), List<Icon>::addAll);
	}

	private static Stream<TooltipMaker> getApplicableMakers(ItemStack stack) {
		return YResources.getRegistryString(stack).map((r) -> Arrays.stream(TooltipMaker.values()).filter((m) -> m.isApplicable(stack, r)).sorted()).orElse(Stream.empty());
	}

	protected abstract boolean isApplicable(ItemStack stack, String registryString);
}
