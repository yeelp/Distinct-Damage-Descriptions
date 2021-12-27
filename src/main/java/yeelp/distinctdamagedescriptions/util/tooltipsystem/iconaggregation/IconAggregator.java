package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Root of the Icon Aggregation hierarchy.
 * 
 * @author Yeelp
 *
 */
public interface IconAggregator {

	/**
	 * Get a list of Icons to draw
	 * 
	 * @param stack    the stack the tooltip is for
	 * @param x        the x coord on screen
	 * @param y        the y coord on screen
	 * @param tooltips the <em>final</em> list of tooltips for this stack
	 * @return a List of Icons to draw
	 */
	List<Icon> getIconsToDraw(ItemStack stack, int x, int y, List<String> tooltips);

	/**
	 * Should these icons be drawn?
	 * 
	 * @return tru if yes, false if no.
	 */
	boolean shouldShowIcons();
}
