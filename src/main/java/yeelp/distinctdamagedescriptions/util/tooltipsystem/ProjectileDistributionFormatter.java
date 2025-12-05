package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * A singleton formatter instance for formatting projectile distributions.
 * 
 * @author Yeelp
 *
 */
public class ProjectileDistributionFormatter extends AbstractDamageDistributionFormatter {

	private static ProjectileDistributionFormatter instance;

	protected ProjectileDistributionFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, (s) -> YResources.getRegistryStringWithMetadata(s).filter(DDDConfigurations.projectiles::isProjectilePairRegistered).map(DDDConfigurations.projectiles::getFromItemID), "projectiledistribution");
	}

	/**
	 * Get the singleton instance of this ProjectileDistributionFormatter if it
	 * exists. If it doesn't exists, a new instance is created and stored, and that
	 * instance is returned.
	 * 
	 * @return The singleton instance, or a new one if it doesn't exist yet.
	 */
	public static ProjectileDistributionFormatter getInstance() {
		return instance == null ? instance = new ProjectileDistributionFormatter() : instance;
	}

	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return true;
	}

	@Override
	protected float getDamageToDistribute(ItemStack stack) {
		return 1.0f;
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.PROJECTILE;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}
}
