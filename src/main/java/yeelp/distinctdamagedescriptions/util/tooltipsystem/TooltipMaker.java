package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import com.google.common.base.Functions;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
		public boolean isApplicable(ItemStack stack, String registryString) {
			return (DDDConfigurations.items.configured(registryString) || stack.hasCapability(DamageDistribution.cap, null)) || (!MOB_DAMAGE.isApplicable(stack, registryString) && ModConfig.client.alwaysShowDamageDistTooltip);
		}
	},
	MOB_DAMAGE(MobDamageDistributionFormatter.getInstance(), MobDamageDistributionIconAggregator.getInstance()) {
		@Override
		public boolean isApplicable(ItemStack stack, String registryString) {
			return ModConfig.client.showMobDamage && stack.getItem() instanceof ItemMonsterPlacer && isMobConfigured(stack, DDDConfigurations.mobDamage);
		}
	},
	PROJECTILE(ProjectileDistributionFormatter.getInstance(), ProjectileDamageDistributionIconAggregator.getInstance()) {
		@Override
		public boolean isApplicable(ItemStack stack, String registryString) {
			return DDDConfigurations.projectiles.isProjectilePairRegistered(registryString);
		}
	},
	ARMOR(ArmorDistributionFormatter.getInstance(), ArmorDistributionIconAggregator.getInstance()) {
		@Override
		public boolean isApplicable(ItemStack stack, String registryString) {
			return ModConfig.resist.enableArmorCalcs && (DDDConfigurations.armors.configured(registryString) || stack.hasCapability(ArmorDistribution.cap, null));
		}
	},
	SHIELD(ShieldDistributionFormatter.getInstance(), ShieldDistributionIconAggregator.getInstance()) {
		@Override
		public boolean isApplicable(ItemStack stack, String registryString) {
			return ModConfig.resist.enableShieldCalcs && (DDDConfigurations.shields.configured(registryString) || stack.hasCapability(ShieldDistribution.cap, null));
		}
	},
	MOB_RESISTANCES(MobResistancesFormatter.getInstance(), MobResistanceIconAggregator.getInstance()) {
		@Override
		public boolean isApplicable(ItemStack stack, String registryString) {
			return stack.getItem() instanceof ItemMonsterPlacer && isMobConfigured(stack, DDDConfigurations.mobResists);
		}
	};

	private final TooltipFormatter<ItemStack> formatter;
	private final IconAggregator aggregator;

	TooltipMaker() {
		this(null, null);
	}

	TooltipMaker(TooltipFormatter<ItemStack> formatter, IconAggregator aggregator) {
		this.formatter = formatter;
		this.aggregator = aggregator;
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
		return getApplicableMakers(stack).map((m) -> m.formatter).<List<String>>collect(LinkedList::new, (l, f) -> l.addAll(f.format(stack)), List::addAll);
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
		return getApplicableMakers(stack).map((m) -> m.aggregator).<List<Icon>>collect(LinkedList::new, (l, a) -> l.addAll(a.getIconsToDraw(stack, x, y, tooltips)), List::addAll);
	}

	private static Stream<TooltipMaker> getApplicableMakers(ItemStack stack) {
		return YResources.getRegistryStringWithMetadata(stack).map((r) -> Arrays.stream(TooltipMaker.values()).filter((m) -> m.isApplicable(stack, r)).sorted()).orElse(Stream.empty());
	}

	public abstract boolean isApplicable(ItemStack stack, String registryString);
	
	protected static boolean isMobConfigured(ItemStack spawnEgg, IDDDConfiguration<?> config) {
		return Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(spawnEgg)).map(Functions.toStringFunction()).filter(config::configured).isPresent();
	}
}
