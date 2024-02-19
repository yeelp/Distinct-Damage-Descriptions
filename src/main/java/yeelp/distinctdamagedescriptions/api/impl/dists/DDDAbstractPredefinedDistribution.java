package yeelp.distinctdamagedescriptions.api.impl.dists;

import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;

/**
 * The ground work of a {@link DDDPredefinedDistribution}.
 * @author Yeelp
 *
 */
public abstract class DDDAbstractPredefinedDistribution implements DDDPredefinedDistribution {

	private final String name;
	private final Source src;
	
	protected DDDAbstractPredefinedDistribution(String name, Source src) {
		this.name = name;
		this.src = src;
	}
	
	@Override
	public Source getCreationSource() {
		return this.src;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
