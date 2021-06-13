package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * The head of the tooltip creation system for DDD
 * @author Yeelp
 *
 */
public class TooltipCreator {
	private static TooltipCreator instance;
	
	private TooltipCreator() {
		
	}
	
	/**
	 * Get the singleton TooltipCreator, or a new instance if not instantiated.
	 * @return The singleton instance, or a new one if it doesn't exist.
	 */
	public static TooltipCreator getInstance() {
		return instance == null ? instance = new TooltipCreator() : instance;
	}
	
	/**
	 * Make a tooltip addition for an ItemStack
	 * @param stack stack to add a tooltip to.
	 * @return A List of Strings with additional information about all the DDD capabilities present on the item, including mob resistances for spawn eggs.
	 */
	public List<String> makeTooltip(ItemStack stack) {
		return getApplicableFormatters(stack).<List<String>>collect(LinkedList<String>::new, (l, f) -> l.addAll(f.format(stack)), List<String>::addAll);
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
	public List<String> makeHwylaTooltip(Entity entity) {
		Optional<MobResistanceCategories> cats = YResources.getEntityIDString(entity).map(DDDConfigurations.mobResists::get);
		if(cats.isPresent()) {
			List<String> tip = HwylaTooltipFormatter.getInstance().format(null);
			HwylaTooltipFormatter.getInstance().formatCapabilityFor(null, cats.get()).ifPresent(tip::addAll);
			return tip;
		}
		else {
			return ImmutableList.of();
		}
	}
	
	private Stream<TooltipFormatter> getApplicableFormatters(ItemStack stack) {
		LinkedList<TooltipFormatter> lst = new LinkedList<TooltipFormatter>();
		String regKey = YResources.getRegistryString(stack);
		if(DDDConfigurations.items.configured(regKey)) {
			lst.add(ItemDistributionFormatter.getInstance());
		}
		if(DDDConfigurations.projectiles.isProjectilePairRegistered(regKey)) {
			lst.add(ProjectileDistributionFormatter.getInstance());
		}
		if(DDDConfigurations.armors.configured(regKey)) {
			lst.add(ArmorDistributionFormatter.getInstance());
		}
		if(DDDConfigurations.shields.configured(regKey)) {
			lst.add(ShieldDistributionFormatter.getInstance());
		}
		if(stack.getItem() instanceof ItemMonsterPlacer && DDDConfigurations.mobResists.configured(ItemMonsterPlacer.getNamedIdFrom(stack).toString())) {
			lst.add(MobResistancesFormatter.getInstance());
		}
		return lst.stream().filter((f) -> f.shouldShow());
	}
}
