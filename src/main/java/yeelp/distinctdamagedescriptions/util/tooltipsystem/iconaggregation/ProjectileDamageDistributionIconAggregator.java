package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ProjectileDistributionFormatter;

/**
 * An icon aggregator for projectile damage
 * @author Yeelp
 *
 */
public class ProjectileDamageDistributionIconAggregator extends DistributionIconAggregator<IDamageDistribution> {

	private static ProjectileDamageDistributionIconAggregator instance;
	
	private ProjectileDamageDistributionIconAggregator() {
		super(ProjectileDistributionFormatter.getInstance(), (s) -> DDDConfigurations.projectiles.getFromItemID(YResources.getRegistryString(s)));
	}
	
	/**
	 * Get the singleton instance
	 * @return the singleton instance
	 */
	public static ProjectileDamageDistributionIconAggregator getInstance() {
		return instance == null ? instance = new ProjectileDamageDistributionIconAggregator() : instance;
	}
}
