package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * A skeletal icon aggregator for capabilities
 * @author Yeelp
 *
 */
public abstract class AbstractCapabilityIconAggregator extends AbstractIconAggregator {

	private final String regex;
	private final Supplier<Boolean> shouldShow;
	
	protected AbstractCapabilityIconAggregator(String regex, Supplier<Boolean> shouldShow) {
		this.regex = regex;
		this.shouldShow = shouldShow;
	}

	@Override
	protected int getStartX(int initialX, List<String> currTooltip) {
		return initialX - 2;
	}

	@Override
	protected int getStartY(int initialY, List<String> currTooltip) {
		//for(Iterator<String> it = currTooltip.iterator(); it.hasNext() && !it.next().matches(this.regex); initialY += AbstractIconAggregator.ICON_HEIGHT);
		Iterator<String> it = currTooltip.iterator();
		while(it.hasNext()) {
			String s = it.next();
			if(s.startsWith(regex)) {
				break;
			}
			initialY += AbstractIconAggregator.ICON_HEIGHT;
		}
		return initialY + AbstractIconAggregator.ICON_HEIGHT + 1;
	}
	
	@Override
	public boolean shouldShowIcons() {
		return this.shouldShow.get();
	}
}
