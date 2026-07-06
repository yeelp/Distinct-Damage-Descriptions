package yeelp.distinctdamagedescriptions.integration.lycanites.dists;

import com.lycanitesmobs.ObjectManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.config.DefaultValues;
import yeelp.distinctdamagedescriptions.config.ModConfig;

import java.util.Optional;

public class LycanitesBleedDistribution extends LycanitesPredefinedDistribution {

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Potion> bleedEffect;
	
	public LycanitesBleedDistribution() {
		super("Lycanites Bleed", () -> ModConfig.compat.lycanites.enableBleedDist, () -> ModConfig.compat.lycanites.bleedDistribution, () -> DefaultValues.LYCANITES_BLEED_DIST);
	}
	
	@Override
	protected boolean isApplicable(DamageSource src, EntityLivingBase target) {
		if(src != DamageSource.MAGIC) {
			return false;
		}
		return this.bleedEffect.filter((pot) -> target.isPotionActive(pot) &&
                target.getEntityWorld().getTotalWorldTime() % 20L == 0L &&
                !target.isRiding() &&
                target.prevDistanceWalkedModified != target.distanceWalkedModified).isPresent();
	}

	@Override
	public void loadModSpecificData() {
		this.bleedEffect = Optional.ofNullable(ObjectManager.getEffect("bleed"));
	}
	
	@Override
	public int priority() {
		return -2;
	}

}
