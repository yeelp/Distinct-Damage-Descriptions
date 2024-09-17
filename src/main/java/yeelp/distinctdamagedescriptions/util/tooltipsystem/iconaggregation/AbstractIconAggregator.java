package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;

/**
 * A skeletal implementation of a IconAggregator. Extenders need to provide
 * implementations to set the starting, x and y values (which may be the same as
 * the ones passed in) and a way to get a set of <b><em>ordered</em></b>
 * {@link DDDDamageType}s that relate to the type icons to display. The order is
 * defined by the natural ordering specified by
 * {@link DDDDamageType#compareTo(DDDDamageType)}
 * 
 * @author Yeelp
 *
 */
public abstract class AbstractIconAggregator implements IconAggregator {

	protected static final int ICON_HEIGHT = 10;

	@Override
	public List<Icon> getIconsToDraw(ItemStack stack, int x, int y, List<String> tooltips) {
		if(this.shouldShowIcons()) {
			final int xVal = this.getStartX(x, tooltips);
			Iterator<Integer> ys = Stream.iterate(this.getStartY(y, tooltips), (i) -> i + ICON_HEIGHT).iterator();
			return this.getOrderedTypes(stack).collect(LinkedList<Icon>::new, (l, d) -> l.add(new Icon(xVal, ys.next(), getUFromType(d))), List<Icon>::addAll);
		}
		return ImmutableList.of();
	}

	/**
	 * Get the starting X value to draw tooltips icons at.
	 * 
	 * @param initialX    the initial x value passed in by the tooltip event
	 * @param currTooltip the current list of tooltips passed in by the tooltip
	 *                    event.
	 * @return The starting X value
	 */
	protected abstract int getStartX(int initialX, List<String> currTooltip);

	/**
	 * Get the starting Y value to draw tooltip icons at
	 * 
	 * @param initialY    the initial y value passed in by the tooltip event
	 * @param currTooltip the current list of tooltips passed in by the tooltip
	 *                    event
	 * @return the starting y value
	 */
	protected abstract int getStartY(int initialY, List<String> currTooltip);

	/**
	 * Get a stream of types to draw in order
	 * 
	 * @param stack the stack to get icons for
	 * @return a Stream of ordered damage types to draw icons for.
	 */
	protected abstract Stream<DDDDamageType> getOrderedTypes(ItemStack stack);

	private static int getUFromType(DDDDamageType type) {
		return ICON_HEIGHT * Arrays.binarySearch(DDDBuiltInDamageType.BUILT_IN_TYPES, type);
	}
}
