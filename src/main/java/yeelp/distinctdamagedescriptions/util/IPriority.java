package yeelp.distinctdamagedescriptions.util;

/**
 * A priority system. Objects with higher priority will appear first in a sorted
 * list. This effectively uses reverse ordering on integers.
 * 
 * @author Yeelp
 *
 */
public interface IPriority extends Comparable<IPriority> {

	/**
	 * The priority this object has. Objects with higher priority will be handled
	 * before objects of lower priority. Specific implementation determines what
	 * happens with objects of the same priority.
	 * 
	 * @return the priority
	 */
	default int priority() {
		return 0;
	}
	
	@Override
	default int compareTo(IPriority o) {
		return Integer.compare(o.priority(), this.priority());
	}

}
