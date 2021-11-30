package yeelp.distinctdamagedescriptions.integration.client;

import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;

/**
 * An extension of the tooltip formatting system for special mod compat.
 * @author Yeelp
 *
 * @param <T> type this formatter formats.
 */
public interface IModCompatTooltipFormatter<T> extends TooltipFormatter<T>, Comparable<IModCompatTooltipFormatter<?>>{
	
	/**
	 * Is this formatter applicable for this object
	 * @param t the object
	 * @return true if this formatter is for this mod compat object.
	 */
	boolean applicable(T t);
	
	/**
	 * Get the icon aggregator for this formatter.
	 * @returnThe aggregator
	 */
	IconAggregator getIconAggregator();

	@Override
	default int compareTo(IModCompatTooltipFormatter<?> o) {
		return this.getType().compareTo(o.getType());
	}
	
}
