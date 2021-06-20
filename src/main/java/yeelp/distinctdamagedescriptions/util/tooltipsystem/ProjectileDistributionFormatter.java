package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * A singleton formatter instance for formatting projectile distributions.
 * @author Yeelp
 *
 */
public class ProjectileDistributionFormatter extends AbstractDamageDistributionFormatter {
	
	private static ProjectileDistributionFormatter instance;
	
	private ProjectileDistributionFormatter() {	
		super(KeyTooltip.CTRL, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, (s) -> DDDConfigurations.projectiles.getFromItemID(YResources.getRegistryString(s.getItem())), new TextComponentTranslation("tooltips.distinctdamagedescriptions.projectiledistribution").setStyle(new Style().setColor(TextFormatting.GRAY)));
	}
	
	/**
	 * Get the singleton instance of this ProjectileDistributionFormatter if it exists.
	 * If it doesn't exists, a new instance is created and stored, and that instance is returned.
	 * @return The singleton instance, or a new one if it doesn't exist yet.
	 */
	public static ProjectileDistributionFormatter getInstance() {
		return instance == null ? instance = new ProjectileDistributionFormatter() : instance;
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return f != DDDNumberFormatter.PLAIN;
	}

	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return true;
	}
}
