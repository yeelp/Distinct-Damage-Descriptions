package yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation;

import java.util.Optional;

import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.MobDamageDistributionFormatter;

/**
 * An icon aggregator for mob damage distributions
 * @author Yeelp
 *
 */
public class MobDamageDistributionIconAggregator extends DistributionIconAggregator<IDamageDistribution> {
	private static MobDamageDistributionIconAggregator instance;
	
	private MobDamageDistributionIconAggregator () {
		super(MobDamageDistributionFormatter.getInstance(), (s) -> Optional.ofNullable(DDDConfigurations.mobDamage.get(Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(s)).map(ResourceLocation::toString).orElse(""))));
	}
	
	/**
	 * Get the singleton instance
	 * @return the singletons instance
	 */
	public static MobDamageDistributionIconAggregator getInstance() {
		return instance == null ? instance = new MobDamageDistributionIconAggregator() : instance;
	}
}
